import React from "react";
import styles from "./Thread.module.css";
import { ThreadDto } from "@/be/jinear-core";
import InitialMessage from "@/components/channelScreen/channelBody/thread/initialMessage/InitialMessage";
import ReplyInput from "@/components/channelScreen/channelBody/thread/replyInput/ReplyInput";
import Line from "@/components/line/Line";
import Logger from "@/utils/logger";
import MessageList from "@/components/channelScreen/channelBody/thread/messageList/MessageList";

interface ThreadProps {
  thread: ThreadDto;
  canReplyThreads: boolean;
  workspaceName: string;
  viewingAsDetail: boolean;
}

const logger = Logger("Thread");

const Thread: React.FC<ThreadProps> = ({ thread, canReplyThreads, workspaceName, viewingAsDetail = false }) => {

  return (
    <div className={styles.container}>
      <InitialMessage
        message={thread.threadMessageInfo.initialMessage}
        workspaceName={workspaceName}
        channelId={thread.channelId}
        viewingAsDetail={viewingAsDetail}
      />
      <MessageList thread={thread} workspaceName={workspaceName} viewingAsDetail={viewingAsDetail} />
      {canReplyThreads && <>
        <Line />
        <div className={"spacer-h-1"} />
        <ReplyInput threadId={thread.threadId} />
      </>
      }
    </div>
  );
};

export default Thread;