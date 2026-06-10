import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import {VAPID_PUBLIC_KEY} from "@/components/firebaseConfiguration/FirebaseConfigration";
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
import {getToken} from "firebase/messaging";
import useTranslation from "@/locales/useTranslation";
import React, {useEffect, useState} from "react";
import {IoNotifications} from "react-icons/io5";
import Modal from "../modal/Modal";
import styles from "./NotificationPermissionModal.module.css";
import {setLocalStorage} from "@/hooks/useLocalStorage";

interface NotificationPermissionModalProps {
}

const logger = Logger("NotificationPermissionModal");

export const NOTIFICATIONS_REJECT_KEY = "do-not-ask-notifications";

const NotificationPermissionModal: React.FC<NotificationPermissionModalProps> = ({}) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const title = t("notificationPermissionModalTitle");
    const visible = useTypedSelector(selectNotificationPermissionModalVisible);
    const currentAccountId = useTypedSelector(selectCurrentAccountId);
    const [initializing, setInitializing] = useState<boolean>(false);
    const [initializeNotificationTarget, {isSuccess: isInitNotifTargetSuccess, isLoading: isInitNotifTargetLoading}] =
        useInitializeNotificationTargetMutation();
    const platform = useTypedSelector(selectNotificationPermissionModalPlatform);

    const messaging = useTypedSelector(selectMessaging);

    const close = () => {
        setInitializing(false);
        dispatch(closeNotificationPermissionModal());
    };

    useEffect(() => {
        if (!isInitNotifTargetLoading && isInitNotifTargetSuccess) {
            close();
        }
        setInitializing(isInitNotifTargetLoading);
    }, [isInitNotifTargetSuccess, isInitNotifTargetLoading, close]);

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
            logger.log(`Failed to get notification permission. ${error}`);
            close();
        }
    };

    const attachAccount = async (accountId: string) => {
        if (messaging) {
            const serviceWorkerRegistration = await navigator.serviceWorker.ready;
            const currentFirebaseToken = await getToken(messaging, {
                vapidKey: VAPID_PUBLIC_KEY,
                serviceWorkerRegistration
            });
            logger.log(
                `Firebase token retrieved attaching now. accountId: ${accountId}, currentFirebaseToken: ${currentFirebaseToken}`
            );
            if (currentFirebaseToken) {
                logger.log(`Attach notification target api call has started.`);
                initializeNotificationTarget({externalTargetId: currentFirebaseToken, providerType: "FIREBASE"});
            }
        }
    };

    return (
        <Modal
            visible={visible}
            // title={title}
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
                    disabled={initializing}
                    loading={initializing}
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
