import { FeedItemMessage } from "@/model/be/jinear-core";
import { format } from "date-fns";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./MessageHeader.module.css";

interface MessageHeaderProps {
  message: FeedItemMessage;
}

const MessageHeader: React.FC<MessageHeaderProps> = ({ message }) => {
  const { t } = useTranslation();

  return (
    <div className={styles.container}>
      <div className={styles.fromContainer}>
        <div className={styles.name}>{message.from?.name}</div>
        <div className={styles.address}>{message.from?.address}</div>
      </div>
      <div className={styles.date}>{message.date ? format(new Date(message.date), t("dateTimeFormat")) : ""}</div>
    </div>
  );
};

export default MessageHeader;
