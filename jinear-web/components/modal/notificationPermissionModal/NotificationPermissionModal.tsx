import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { VAPID_PUBLIC_KEY } from "@/components/firebaseConfiguration/FirebaseConfigration";
import { useInitializeNotificationTargetMutation } from "@/store/api/notificationTargetApi";
import { selectCurrentAccountId } from "@/store/slice/accountSlice";
import { selectMessaging } from "@/store/slice/firebaseSlice";
import {
  closeNotificationPermissionModal,
  selectNotificationPermissionModalPlatform,
  selectNotificationPermissionModalVisible,
} from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import Logger from "@/utils/logger";
import { submitAskPermissionsAndSendTokenEvent } from "@/utils/webviewUtils";
import { getToken } from "firebase/messaging";
import useTranslation from "locales/useTranslation";
import React, { useEffect, useState } from "react";
import { IoNotifications } from "react-icons/io5";
import Modal from "../modal/Modal";
import styles from "./NotificationPermissionModal.module.css";

interface NotificationPermissionModalProps {}

const logger = Logger("NotificationPermissionModal");

const NotificationPermissionModal: React.FC<NotificationPermissionModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const title = t("notificationPermissionModalTitle");
  const visible = useTypedSelector(selectNotificationPermissionModalVisible);
  const currentAccountId = useTypedSelector(selectCurrentAccountId);
  const [initializing, setInitializing] = useState<boolean>(false);
  const [initializeNotificationTarget, { isSuccess: isInitNotifTargetSuccess, isLoading: isInitNotifTargetLoading }] =
    useInitializeNotificationTargetMutation();
  const platform = useTypedSelector(selectNotificationPermissionModalPlatform);

  const messaging = useTypedSelector(selectMessaging);

  useEffect(() => {
    if (!isInitNotifTargetLoading && isInitNotifTargetSuccess) {
      close();
    }
    setInitializing(isInitNotifTargetLoading);
  }, [isInitNotifTargetSuccess, isInitNotifTargetLoading]);

  const close = () => {
    setInitializing(false);
    dispatch(closeNotificationPermissionModal());
  };

  const askPermissions = async () => {
    if (platform == "expo-webview") {
      submitAskPermissionsAndSendTokenEvent();
      return;
    }
    logger.log(`Ask permission has started. Showing native prompt.`);
    const notificationPermission = await Notification.requestPermission();
    logger.log(`Retrieved notification permission. ${notificationPermission}`);
    if (notificationPermission == "granted" && currentAccountId) {
      logger.log(`Attaching account. ${currentAccountId} - ${notificationPermission}`);
      await attachAccount(currentAccountId);
    }
  };

  const attachAccount = async (accountId: string) => {
    if (messaging) {
      setInitializing(true);
      const currentFirebaseToken = await getToken(messaging, { vapidKey: VAPID_PUBLIC_KEY });
      logger.log(
        `Firebase token retrieved attaching now. accountId: ${accountId}, currentFirebaseToken: ${currentFirebaseToken}`
      );
      if (currentFirebaseToken) {
        logger.log(`Attach notification target api call has started.`);
        initializeNotificationTarget({ externalTargetId: currentFirebaseToken, providerType: "FIREBASE" });
      }
    }
  };

  return (
    <Modal visible={visible} title={title} bodyClass={styles.container} requestClose={close} width={"medium-fixed"}>
      <div className={styles.body}>
        <IoNotifications size={32} />
        {t("notificationPermissionModalInfoText")}
      </div>

      <div className={styles.actionBar}>
        <Button
          disabled={initializing}
          loading={initializing}
          heightVariant={ButtonHeight.mid}
          variant={ButtonVariants.contrast}
          onClick={askPermissions}
        >
          {t("notificationPermissionModalAllowPermissions")}
        </Button>

        <Button
          disabled={isInitNotifTargetLoading}
          heightVariant={ButtonHeight.mid}
          variant={ButtonVariants.default}
          onClick={close}
        >
          {t("notificationPermissionModalDismiss")}
        </Button>
      </div>
    </Modal>
  );
};

export default NotificationPermissionModal;
