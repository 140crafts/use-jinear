import Line from "@/components/line/Line";
import { NotificationMessageDto, NotificationTemplateType } from "@/model/be/jinear-core";
import { format } from "date-fns";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoAlarmOutline, IoCaretForwardCircleOutline, IoRadioButtonOn } from "react-icons/io5";
import styles from "./NotificationMessageRow.module.scss";

interface NotificationMessageRowProps {
  notificationMessage: NotificationMessageDto;
  withDateTitle: boolean;
}

const NOTIFICATION_ICON_MAP = {
  TASK_REMINDER: IoAlarmOutline,
  TASK_ASSIGNED: IoCaretForwardCircleOutline,
  default: IoRadioButtonOn,
};

export const getNotificationIcon = (templateType?: NotificationTemplateType) => {
  return templateType ? NOTIFICATION_ICON_MAP?.[templateType] || NOTIFICATION_ICON_MAP.default : NOTIFICATION_ICON_MAP.default;
};

const NotificationMessageRow: React.FC<NotificationMessageRowProps> = ({ notificationMessage, withDateTitle = false }) => {
  const { t } = useTranslation();
  const Icon = getNotificationIcon(notificationMessage?.templateType);
  return (
    <>
      {withDateTitle && (
        <div className={styles.dateTitle}>{format(new Date(notificationMessage.createdDate), t("dateFormat"))}</div>
      )}
      <div className={styles.container}>
        <div>
          <Icon size={17} />
        </div>
        <div>
          <b>{notificationMessage.title}</b>
        </div>
        <div className={styles.text}>{notificationMessage.text}</div>
        <div className={styles.date}>{format(new Date(notificationMessage.createdDate), t("dateTimeFormat"))}</div>
      </div>
      <Line className={styles.line} />
    </>
  );
};

export default NotificationMessageRow;
