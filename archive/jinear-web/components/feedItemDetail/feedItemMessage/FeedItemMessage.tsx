import { FeedItemMessage } from "@/model/be/jinear-core";
import React from "react";
import styles from "./FeedItemMessage.module.css";
import MessageContent from "./messageContent/MessageContent";
import MessageHeader from "./messageHeader/MessageHeader";

interface FeedItemMessageProps {
  message: FeedItemMessage;
  isLast: boolean;
}

const FeedItemMessage: React.FC<FeedItemMessageProps> = ({ message, isLast }) => {
  return (
    <>
      <div id={isLast ? `feed-last-message` : undefined} className={styles.container}>
        <MessageHeader message={message} />
        <MessageContent message={message} />
      </div>
      <div className={styles.divider} />
    </>
  );
};

export default FeedItemMessage;
