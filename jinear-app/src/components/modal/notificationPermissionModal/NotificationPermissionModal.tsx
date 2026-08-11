import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import {useInitializeNotificationTargetMutation} from "@/store/api/notificationTargetApi";
import {selectCurrentAccountId} from "@/store/slice/accountSlice";
import {selectMessaging} from "@/store/slice/firebaseSlice";
import {
    closeNotificationPermissionModal,
    selectNotificationPermissionModalPlatform,
    selectNotificationPermissionModalVisible
} from "@/store/slice/modalSlice";
import {useAppDispatch, useTypedSelector} from "@/store";
import Logger from "@/util/logger";
import {submitAskPermissionsAndSendTokenEvent} from "@/util/webviewUtils";
import useTranslation from "@/locales/useTranslation";
import React, {useCallback, useEffect, useState} from "react";
import {IoNotifications} from "react-icons/io5";
import Modal from "../modal/Modal";
import styles from "./NotificationPermissionModal.module.css";
import {setLocalStorage} from "@/hooks/useLocalStorage";
import {NOTIFICATIONS_REJECT_KEY, readNotificationPermission} from "@/util/notificationPermission";
import {getFirebaseNotificationToken} from "@/util/attachNotificationTarget";

interface NotificationPermissionModalProps {
}

const logger = Logger("NotificationPermissionModal");

const NotificationPermissionModal: React.FC<NotificationPermissionModalProps> = ({}) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const visible = useTypedSelector(selectNotificationPermissionModalVisible);
    const currentAccountId = useTypedSelector(selectCurrentAccountId);
    const [initializing, setInitializing] = useState<boolean>(false);
    const [initializeNotificationTarget, {isSuccess: isInitNotifTargetSuccess, isLoading: isInitNotifTargetLoading}] =
        useInitializeNotificationTargetMutation();
    const platform = useTypedSelector(selectNotificationPermissionModalPlatform);

    const messaging = useTypedSelector(selectMessaging);

    const close = useCallback(() => {
        setInitializing(false);
        dispatch(closeNotificationPermissionModal());
    }, [dispatch]);

    // Never leaves the modal hanging: a missing `messaging` or a failing getToken
    // used to abandon the flow with the spinner still spinning and nothing logged.
    const attachAccount = async (accountId: string) => {
        if (!messaging) {
            logger.error(`Messaging is not initialized, cannot attach notification target.`);
            close();
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
                return;
            }
            logger.error(`Firebase returned an empty token.`);
            close();
        } catch (error) {
            logger.error({message: "Attaching notification target failed.", error});
            close();
        }
    };

    useEffect(() => {
        if (isInitNotifTargetSuccess) {
            close();
        }
    }, [isInitNotifTargetSuccess, close]);

    // Backstop against a stale `Notification.permission` read elsewhere: whatever
    // opened this modal, never show it to someone whose browser has already
    // decided. On "granted" we quietly (re)register the token instead.
    useEffect(() => {
        if (!visible) {
            return;
        }
        let cancelled = false;
        void readNotificationPermission().then((permission) => {
            if (cancelled || permission == "default") {
                return;
            }
            logger.log(`Permission is already "${permission}", closing modal without prompting.`);
            if (permission == "granted" && currentAccountId) {
                void attachAccount(currentAccountId);
            } else {
                close();
            }
        });
        return () => {
            cancelled = true;
        };
    }, [visible, currentAccountId, messaging, close]);

    const rejectAndClose = () => {
        setLocalStorage({key: NOTIFICATIONS_REJECT_KEY, value: true});
        close();
    };

    const requestPermissionWithTimeout = (timeoutMs: number): Promise<NotificationPermission> => {
        return Promise.race([
            Notification.requestPermission(),
            new Promise<never>((_, reject) =>
                setTimeout(() => reject(new Error("Notification permission request timed out.")), timeoutMs)
            )
        ]);
    };

    const askPermissions = async () => {
        if (platform == "expo-webview") {
            submitAskPermissionsAndSendTokenEvent();
            return;
        }
        logger.log(`Ask permission has started. Showing native prompt.`);
        try {
            setInitializing(true);
            const notificationPermission = await requestPermissionWithTimeout(30000);
            logger.log(`Retrieved notification permission. ${notificationPermission}`);
            if (notificationPermission == "granted" && currentAccountId) {
                logger.log(`Attaching account. ${currentAccountId} - ${notificationPermission}`);
                await attachAccount(currentAccountId);
            } else {
                close();
            }
        } catch (error) {
            logger.error({message: "Failed to get notification permission.", error});
            close();
        }
    };

    return (
        <Modal
            visible={visible}
            bodyClass={styles.container}
            requestClose={close}
            width={"medium-fixed"}
        >
            <div className={styles.body}>
                <IoNotifications size={32}/>
                {t("notificationPermissionModalInfoText")}
            </div>

            <div className={styles.actionBar}>
                <Button
                    disabled={isInitNotifTargetLoading}
                    heightVariant={ButtonHeight.mid}
                    variant={ButtonVariants.default}
                    onClick={rejectAndClose}
                >
                    {t("notificationPermissionModalDismissAndDoNotAskAgain")}
                </Button>

                <Button
                    disabled={isInitNotifTargetLoading}
                    heightVariant={ButtonHeight.mid}
                    variant={ButtonVariants.filled}
                    onClick={close}
                >
                    {t("notificationPermissionModalLater")}
                </Button>

                <Button
                    disabled={initializing || isInitNotifTargetLoading}
                    loading={initializing || isInitNotifTargetLoading}
                    heightVariant={ButtonHeight.mid}
                    variant={ButtonVariants.contrast}
                    onClick={askPermissions}
                >
                    {t("notificationPermissionModalAllowPermissions")}
                </Button>
            </div>
        </Modal>
    );
};

export default NotificationPermissionModal;
