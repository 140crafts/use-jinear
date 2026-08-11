import {api} from "@/store/api/api";
import {useInitializeNotificationTargetMutation} from "@/store/api/notificationTargetApi";
import {selectAuthState, selectCurrentAccountId, selectCurrentSessionId} from "@/store/slice/accountSlice";
import {selectFirebase, selectMessaging, setFirebase, setMessaging} from "@/store/slice/firebaseSlice";
import {closeNotificationPermissionModal, popNotificationPermissionModal} from "@/store/slice/modalSlice";
import {markHasUnreadNotification} from "@/store/slice/taskAdditionalDataSlice";
import {useAppDispatch, useTypedSelector} from "@/store";
import Logger from "@/util/logger";
import {initializeApp} from "firebase/app";
import {deleteToken, getMessaging, isSupported, type MessagePayload, onMessage} from "firebase/messaging";
import React, {useEffect, useRef} from "react";
import {toast} from "react-hot-toast";
import ForegroundNotification from "../foregroundNotification/ForegroundNotification";
import {localStorageItemBooleanParser, useLocalStorage} from "@/hooks/useLocalStorage";
import {
    NOTIFICATIONS_REJECT_KEY,
    onNotificationPermissionChange,
    readNotificationPermission
} from "@/util/notificationPermission";
import {getFirebaseNotificationToken} from "@/util/attachNotificationTarget";
import {firebaseConfig} from "@/util/firebaseConfig";
import {useNavigate} from "react-router-dom";

interface FirebaseConfigrationProps {
}

const logger = Logger("FirebaseConfigration");

const TASK_UPDATE_NOTIFICATIONS = [
    "EDIT_TASK_TITLE",
    "EDIT_TASK_DESC",
    "TASK_UPDATE_TOPIC",
    "TASK_UPDATE_WORKFLOW_STATUS",
    "TASK_CHANGE_ASSIGNEE",
    "TASK_CHANGE_ASSIGNED_DATE",
    "TASK_CHANGE_DUE_DATE",
    "RELATION_INITIALIZED",
    "RELATION_REMOVED",
    "CHECKLIST_INITIALIZED",
    "CHECKLIST_REMOVED",
    "CHECKLIST_TITLE_CHANGED",
    "CHECKLIST_ITEM_CHECKED_STATUS_CHANGED",
    "CHECKLIST_ITEM_LABEL_CHANGED",
    "CHECKLIST_ITEM_REMOVED",
    "CHECKLIST_ITEM_INITIALIZED",
    "TASK_NEW_COMMENT",
    "TASK_ATTACHMENT_ADDED",
    "TASK_ATTACHMENT_DELETED"
];

// getToken can fail transiently (service worker not ready yet, network). Retry a
// few times, then stop; a missing VAPID key or a blocked project would otherwise
// spin forever.
const ATTACH_MAX_ATTEMPTS = 4;
const ATTACH_RETRY_DELAY_MS = 2500;

const FirebaseConfigration: React.FC<FirebaseConfigrationProps> = ({}) => {
    const dispatch = useAppDispatch();
    const navigate = useNavigate();
    const authState = useTypedSelector(selectAuthState);
    const currentAccountId = useTypedSelector(selectCurrentAccountId);
    const currentSessionId = useTypedSelector(selectCurrentSessionId);

    const firebaseApp = useTypedSelector(selectFirebase);
    const messaging = useTypedSelector(selectMessaging);

    const attachRetryTimeout = useRef<ReturnType<typeof setTimeout> | undefined>(undefined);

    const [initializeNotificationTarget, {}] = useInitializeNotificationTargetMutation();
    const doNotAskNotifications = useLocalStorage({
        key: NOTIFICATIONS_REJECT_KEY,
        parser: localStorageItemBooleanParser
    }) ?? false;

    const attachAccount = async (accountId: string, attempt: number = 1) => {
        if (!messaging) {
            logger.log(`Messaging is not ready yet, skipping attach.`);
            return;
        }
        try {
            const currentFirebaseToken = await getFirebaseNotificationToken(messaging);
            logger.log(
                `Firebase token retrieved attaching now. accountId: ${accountId}, currentFirebaseToken: ${currentFirebaseToken}`
            );
            if (currentFirebaseToken) {
                logger.log(`Attach notification target api call has started.`);
                initializeNotificationTarget({externalTargetId: currentFirebaseToken, providerType: "FIREBASE"});
            }
        } catch (error) {
            logger.error({message: `Attaching notification target failed. attempt: ${attempt}`, error});
            if (attempt >= ATTACH_MAX_ATTEMPTS) {
                logger.error(`Giving up on attaching notification target after ${attempt} attempts.`);
                return;
            }
            if (attachRetryTimeout.current) {
                clearTimeout(attachRetryTimeout.current);
            }
            attachRetryTimeout.current = setTimeout(() => {
                attachAccount(accountId, attempt + 1);
            }, ATTACH_RETRY_DELAY_MS * attempt);
        }
    };

    useEffect(() => {
        initializeFirebase();
    }, []);

    useEffect(() => {
        if (messaging && currentSessionId) {
            onMessage(messaging, onForegroundMessage);
        }
    }, [messaging, currentSessionId]);

    useEffect(() => {
        if (firebaseApp) {
            if (firebaseApp && currentAccountId && authState == "LOGGED_IN") {
                checkAndPrompt(currentAccountId);
            } else if (firebaseApp && messaging && authState == "NOT_LOGGED_IN") {
                detachAccount();
            }
        }
    }, [currentAccountId, authState, firebaseApp, messaging]);

    // React to permission changes made outside the modal: browser UI, another
    // tab, or a browser auto-revoke. Without this the app only learns about a
    // grant on the next reload, and a modal opened off a stale read would sit
    // there after the user has already allowed notifications.
    useEffect(() => {
        return onNotificationPermissionChange((permission) => {
            logger.log(`Notification permission changed to ${permission}.`);
            if (permission != "default") {
                dispatch(closeNotificationPermissionModal());
            }
            if (permission == "granted" && currentAccountId && authState == "LOGGED_IN") {
                attachAccount(currentAccountId);
            }
        });
    }, [currentAccountId, authState, messaging]);

    useEffect(() => {
        return () => {
            if (attachRetryTimeout.current) {
                clearTimeout(attachRetryTimeout.current);
            }
        };
    }, []);

    const initializeFirebase = async () => {
        logger.log(`initializeFirebase has started.`);
        const isSupportedBrowser = await isSupported();
        logger.log(`isSupportedBrowser: ${isSupportedBrowser}`);
        if (isSupportedBrowser) {
            const app = initializeApp(firebaseConfig);
            const messaging = getMessaging(app);
            dispatch(setFirebase(app));
            dispatch(setMessaging(messaging));
        }
    };

    const checkAndPrompt = async (currentAccountId: string) => {
        const notificationPermission = await readNotificationPermission();
        logger.log(`Resolved notification permission: ${notificationPermission}`);
        if (notificationPermission == "granted") {
            attachAccount(currentAccountId);
        } else if (notificationPermission == "default" && !doNotAskNotifications) {
            dispatch(popNotificationPermissionModal({visible: true, platform: "web"}));
        }
    };

    const detachAccount = async () => {
        logger.log(`Detach firebase messaging has started.`);
        if (messaging) {
            try {
                deleteToken(messaging);
            } catch (e) {
                console.error(e);
            }
        }
    };

    const onForegroundMessage = (payload: MessagePayload) => {
        logger.log({onForegroundMessage: payload});
        if (payload.notification) {
            const {title = "", body = ""} = payload.notification;
            const launchUrl = payload?.data?.launchUrl;
            const senderSessionInfoId = payload?.data?.senderSessionInfoId;
            if (currentSessionId != senderSessionInfoId) {
                toast((t) => <ForegroundNotification title={title} body={body} launchUrl={launchUrl}/>, {
                    position: window.innerWidth < 768 ? "top-center" : "top-right",
                    duration: 6000
                });
                const notification = new Notification(title, {
                    body,
                    icon: "https://jinear.co/icons/notification-icon.png"
                });
                if (launchUrl) {
                    notification.addEventListener("click", () => {
                        navigate(launchUrl);
                    });
                }
            }

            const notificationType = payload?.data?.notificationType || "";
            dispatch(api.util.invalidateTags(["v1/notification/event/{workspaceId}/unread-count"]));
            if (notificationType == "TASK_INITIALIZED") {
                dispatch(api.util.invalidateTags(["v1/task/list/filter", "v1/calendar/event/filter"]));
            }
            if (
                currentSessionId != senderSessionInfoId &&
                payload?.data?.taskId &&
                TASK_UPDATE_NOTIFICATIONS.indexOf(notificationType) != -1
            ) {
                dispatch(markHasUnreadNotification({taskId: payload?.data?.taskId}));
            }
        }
    };

    return null;
};

export default FirebaseConfigration;
