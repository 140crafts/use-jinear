import React from "react";
import styles from "./ThreadConversation.module.css";
import { ThreadDto } from "@/be/jinear-core";

interface ThreadConversationProps {
  thread: ThreadDto;
}

const ThreadConversation: React.FC<ThreadConversationProps> = ({ thread }) => {

  return (
    <div className={styles.container}>

    </div>
  );
};

export default ThreadConversation;