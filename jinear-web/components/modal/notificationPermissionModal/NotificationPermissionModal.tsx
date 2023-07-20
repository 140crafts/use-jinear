import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { useInitializeNotificationTargetMutation } from "@/store/api/notificationTargetApi";
import { selectCurrentAccountId } from "@/store/slice/accountSlice";
import { closeNotificationPermissionModal, selectNotificationPermissionModalVisible } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import Logger from "@/utils/logger";
import useTranslation from "locales/useTranslation";
import React, { useEffect } from "react";
import { IoNotifications } from "react-icons/io5";
import OneSignal from "react-onesignal";
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
  const [initializeNotificationTarget, { isSuccess: isInitNotifTargetSuccess, isLoading: isInitNotifTargetLoading }] =
    useInitializeNotificationTargetMutation();

  useEffect(() => {
    if (!isInitNotifTargetLoading && isInitNotifTargetSuccess) {
      close();
    }
  }, [isInitNotifTargetSuccess, isInitNotifTargetLoading]);

  const close = () => {
    dispatch(closeNotificationPermissionModal());
  };

  const askPermissions = async () => {
    logger.log(`Ask permission has started. Showing native prompt.`);
    // await OneSignal.showSlidedownPrompt();
    await Notification.requestPermission();
    logger.log(`Native prompt shown. Getting notification permission.`);
    const notificationPermission = await OneSignal.getNotificationPermission();
    logger.log(`Retrieved notification permission. ${notificationPermission}`);
    if (notificationPermission == "granted" && currentAccountId) {
      logger.log(`Attaching account. ${currentAccountId} - ${notificationPermission}`);
      await attachAccount(currentAccountId);
    }
  };

  const attachAccount = async (accountId: string) => {
    logger.log(`Attaching account. accountId: ${accountId}`);
    OneSignal.setSubscription(true);
    const userId = await OneSignal.getUserId();
    OneSignal.setExternalUserId(accountId);
    logger.log(`Setting OneSignal account. accountId: ${accountId}, oneSignalUserId: ${userId}`);
    if (userId) {
      logger.log(`Attach notification target api call has started.`);
      initializeNotificationTarget({ externalTargetId: userId });
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
          disabled={isInitNotifTargetLoading}
          loading={isInitNotifTargetLoading}
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
