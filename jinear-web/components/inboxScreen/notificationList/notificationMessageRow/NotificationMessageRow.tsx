import Button from "@/components/button";
import Line from "@/components/line/Line";
import { NotificationEventDto, NotificationType } from "@/model/be/jinear-core";
import Logger from "@/utils/logger";
import { format } from "date-fns";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoAlarmOutline, IoNotificationsOutline, IoReaderOutline } from "react-icons/io5";
import styles from "./NotificationMessageRow.module.scss";

interface NotificationMessageRowProps {
  notificationEvent: NotificationEventDto;
  withDateTitle: boolean;
}

const NOTIFICATION_ICON_MAP = {
  TASK_REMINDER: IoAlarmOutline,
  WORKSPACE_ACTIVITY: IoReaderOutline,
  default: IoNotificationsOutline,
};

const logger = Logger("NotificationMessageRow");

export const getNotificationIcon = (templateType?: NotificationType) => {
  logger.log(templateType);
  return templateType ? NOTIFICATION_ICON_MAP?.[templateType] || NOTIFICATION_ICON_MAP.default : NOTIFICATION_ICON_MAP.default;
};

const NotificationMessageRow: React.FC<NotificationMessageRowProps> = ({ notificationEvent, withDateTitle = false }) => {
  const { t } = useTranslation();
  const Icon = getNotificationIcon(notificationEvent?.notificationType);
  return (
    <>
      {withDateTitle && (
        <div className={styles.dateTitle}>{format(new Date(notificationEvent.createdDate), t("dateFormat"))}</div>
      )}
      <Button className={styles.container} href={notificationEvent.launchUrl}>
        <div>
          <Icon size={17} />
        </div>
        <div>
          <b>{notificationEvent.title}</b>
        </div>
        <div className={styles.text}>{notificationEvent.text}</div>
        <div className={styles.date}>{format(new Date(notificationEvent.createdDate), t("timeFormat"))}</div>
      </Button>
      <Line className={styles.line} />
    </>
  );
};

export default NotificationMessageRow;
