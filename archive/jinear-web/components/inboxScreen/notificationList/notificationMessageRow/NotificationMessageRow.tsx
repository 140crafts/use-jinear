import Button from "@/components/button";
import Line from "@/components/line/Line";
import { NotificationEventDto, NotificationType } from "@/model/be/jinear-core";
import Logger from "@/utils/logger";
import { format } from "date-fns";
import useTranslation from "locales/useTranslation";
import React from "react";
import {
  IoAlarmOutline,
  IoChatboxOutline, IoDocumentAttachOutline,
  IoNotificationsOutline,
  IoReaderOutline,
  IoTrashBinOutline
} from "react-icons/io5";
import styles from "./NotificationMessageRow.module.scss";
import { LuMessagesSquare } from "react-icons/lu";

interface NotificationMessageRowProps {
  notificationEvent: NotificationEventDto;
  withDateTitle: boolean;
}

const NOTIFICATION_ICON_MAP = {
  TASK_REMINDER: IoAlarmOutline,
  WORKSPACE_ACTIVITY: IoReaderOutline,
  TASK_INITIALIZED: IoReaderOutline,
  TASK_CLOSED: IoReaderOutline,
  EDIT_TASK_TITLE: IoReaderOutline,
  EDIT_TASK_DESC: IoReaderOutline,
  TASK_UPDATE_TOPIC: IoReaderOutline,
  TASK_UPDATE_WORKFLOW_STATUS: IoReaderOutline,
  TASK_CHANGE_ASSIGNEE: IoReaderOutline,
  TASK_CHANGE_ASSIGNED_DATE: IoReaderOutline,
  TASK_CHANGE_DUE_DATE: IoReaderOutline,
  RELATION_INITIALIZED: IoReaderOutline,
  RELATION_REMOVED: IoReaderOutline,
  CHECKLIST_INITIALIZED: IoReaderOutline,
  CHECKLIST_REMOVED: IoReaderOutline,
  CHECKLIST_TITLE_CHANGED: IoReaderOutline,
  CHECKLIST_ITEM_CHECKED_STATUS_CHANGED: IoReaderOutline,
  CHECKLIST_ITEM_LABEL_CHANGED: IoReaderOutline,
  CHECKLIST_ITEM_REMOVED: IoReaderOutline,
  CHECKLIST_ITEM_INITIALIZED: IoReaderOutline,
  TASK_NEW_COMMENT: IoChatboxOutline,
  MESSAGING_NEW_MESSAGE_THREAD:LuMessagesSquare,
  MESSAGING_NEW_MESSAGE_CONVERSATION:LuMessagesSquare,
  TASK_ATTACHMENT_ADDED: IoDocumentAttachOutline,
  TASK_ATTACHMENT_DELETED: IoTrashBinOutline,
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
        <div className={styles.iconContainer}>
          <Icon size={17} />
        </div>
        <div className={styles.contentContainer}>
          <div>
            <b>{notificationEvent.title}</b>
          </div>
          <div className={styles.text}>{notificationEvent.text}</div>
          <div className={styles.date}>{format(new Date(notificationEvent.createdDate), t("timeFormat"))}</div>
        </div>
      </Button>
      <Line className={styles.line} />
    </>
  );
};

export default NotificationMessageRow;
