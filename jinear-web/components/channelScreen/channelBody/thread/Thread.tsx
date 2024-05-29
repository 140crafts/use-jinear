import React from "react";
import styles from "./Thread.module.css";
import InitialMessage from "@/components/channelScreen/channelBody/thread/initialMessage/InitialMessage";
import ReplyInput from "@/components/channelScreen/channelBody/thread/replyInput/ReplyInput";
import Line from "@/components/line/Line";
import Logger from "@/utils/logger";
import MessageList from "@/components/channelScreen/channelBody/thread/messageList/MessageList";
import { useThreadInitialMessage } from "@/hooks/messaging/threadMessage/useThreadInitialMessage";

interface ThreadProps {
  canReplyThreads: boolean;
  threadId: string;
  channelId: string;
  workspaceName: string;
  workspaceId: string;
  viewingAsDetail: boolean;
}

const logger = Logger("Thread");

const Thread: React.FC<ThreadProps> = ({
                                         canReplyThreads,
                                         threadId,
                                         channelId,
                                         workspaceId,
                                         workspaceName,
                                         viewingAsDetail = false
                                       }) => {

  return (
    <div className={styles.container}>
      <InitialMessage
        workspaceName={workspaceName}
        threadId={threadId}
        channelId={channelId}
        viewingAsDetail={viewingAsDetail}
      />
      <MessageList
        channelId={channelId}
        threadId={threadId}
        workspaceId={workspaceId}
        workspaceName={workspaceName}
        viewingAsDetail={viewingAsDetail}
      />
      {canReplyThreads && <>
        <Line />
        <div className={"spacer-h-1"} />
        <ReplyInput
          workspaceId={workspaceId}
          channelId={channelId}
          threadId={threadId}
        />
      </>
      }
    </div>
  );
};

export default Thread;