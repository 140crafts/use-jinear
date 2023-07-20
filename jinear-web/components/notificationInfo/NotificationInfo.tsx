import { popNotificationPermissionModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React, { useEffect, useState } from "react";
import OneSignal from "react-onesignal";
import Button, { ButtonVariants } from "../button";
import styles from "./NotificationInfo.module.css";

interface NotificationInfoProps {}

const NotificationInfo: React.FC<NotificationInfoProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const [oneSignalUserId, setOneSignalUserId] = useState<any>();
  const [notificationPermissionState, setNotificationPermissionState] = useState("");

  useEffect(() => {
    getNotificationPermission();
  }, []);

  useEffect(() => {
    getUserId();
  }, []);

  const getUserId = async () => {
    try {
      const userId = await OneSignal.getUserId();
      setOneSignalUserId(userId);
    } catch (error) {
      setOneSignalUserId(error);
    }
  };

  const getNotificationPermission = () => {
    let message = t("notificationPermission_not_supported");
    try {
      if (typeof window === "object" && typeof Notification === "function") {
        //@ts-ignore
        message = t(`notificationPermission_${Notification.permission}`) || t("notificationPermission_not_supported");
      }
    } catch (error) {
      console.error(error);
    }
    setNotificationPermissionState(message);
  };

  const popModal = () => {
    dispatch(popNotificationPermissionModal());
  };

  return (
    <div className={styles.container}>
      <div>
        oneSignalUserId: <b>{oneSignalUserId}</b>
      </div>
      <div>
        Notifications: <b>{notificationPermissionState}</b>
      </div>
      <Button variant={ButtonVariants.filled} onClick={getNotificationPermission}>
        Reload
      </Button>
      <Button variant={ButtonVariants.filled} onClick={popModal}>
        Pop Perm Modal
      </Button>
    </div>
  );
};

export default NotificationInfo;
