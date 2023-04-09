import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { closeNotificationPermissionModal, selectNotificationPermissionModalVisible } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoNotifications } from "react-icons/io5";
import OneSignal from "react-onesignal";
import Modal from "../modal/Modal";
import styles from "./NotificationPermissionModal.module.css";

interface NotificationPermissionModalProps {}

const NotificationPermissionModal: React.FC<NotificationPermissionModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const title = t("notificationPermissionModalTitle");
  const visible = useTypedSelector(selectNotificationPermissionModalVisible);

  const close = () => {
    dispatch(closeNotificationPermissionModal());
  };

  const askPermissions = async () => {
    await OneSignal.showNativePrompt();
    close();
  };

  return (
    <Modal visible={visible} title={title} bodyClass={styles.container} requestClose={close} width={"default"}>
      <div className={styles.body}>
        <IoNotifications size={32} />
        {t("notificationPermissionModalInfoText")}
      </div>

      <div className={styles.actionBar}>
        <Button heightVariant={ButtonHeight.mid} variant={ButtonVariants.contrast} onClick={askPermissions}>
          {t("notificationPermissionModalAllowPermissions")}
        </Button>

        <Button heightVariant={ButtonHeight.mid} variant={ButtonVariants.default} onClick={close}>
          {t("notificationPermissionModalDismiss")}
        </Button>
      </div>
    </Modal>
  );
};

export default NotificationPermissionModal;
