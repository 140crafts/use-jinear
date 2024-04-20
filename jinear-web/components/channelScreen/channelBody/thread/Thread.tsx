import React from "react";
import styles from "./Thread.module.css";
import { ThreadDto } from "@/be/jinear-core";
import Message from "@/components/channelScreen/channelBody/thread/message/Message";
import ReplyInput from "@/components/channelScreen/channelBody/thread/replyInput/ReplyInput";
import Line from "@/components/line/Line";
import ReplyMessage from "@/components/channelScreen/channelBody/thread/replyMessage/ReplyMessage";
import { useRetrieveThreadMessagesQuery } from "@/api/messageListingApi";
import { isAfter } from "date-fns";
import Logger from "@/utils/logger";

interface ThreadProps {
  thread: ThreadDto;
}

const logger = Logger("Thread");

const Thread: React.FC<ThreadProps> = ({ thread }) => {
  const { data: retrieveThreadMessagesResponse } = useRetrieveThreadMessagesQuery({ threadId: thread.threadId });
  const lastRetrievedMessage = retrieveThreadMessagesResponse?.data?.content?.[0];
  const isThreadInfoFirstAndLastMessageIsDifferent = thread.threadMessageInfo.latestMessage.messageId != thread.threadMessageInfo.initialMessage.messageId;
  const isLastRetrievedMessageIsAfterThreadInfoLastMessage = lastRetrievedMessage && isAfter(new Date(lastRetrievedMessage?.createdDate), new Date(thread.threadMessageInfo.latestMessage.createdDate));
  const actualLatestMessage = isThreadInfoFirstAndLastMessageIsDifferent ?
    (isLastRetrievedMessageIsAfterThreadInfoLastMessage ? lastRetrievedMessage : thread.threadMessageInfo.latestMessage) :
    null;

  logger.log({
    lastRetrievedMessage,
    infoLatestMessage: thread.threadMessageInfo.latestMessage,
    isAfter: lastRetrievedMessage?.createdDate && isAfter(new Date(lastRetrievedMessage?.createdDate), new Date(thread.threadMessageInfo.latestMessage.createdDate)),
    actualLatestMessage,
    isThreadInfoFirstAndLastMessageIsDifferent,
    isLastRetrievedMessageIsAfterThreadInfoLastMessage
  });

  return (
    <div className={styles.container}>
      <Message message={thread.threadMessageInfo.initialMessage} />
      {actualLatestMessage &&
        <>
          <Line />
          <ReplyMessage key={actualLatestMessage.messageId} replyMessage={actualLatestMessage} />
        </>}
      <Line />
      <ReplyInput threadId={thread.threadId} />
    </div>
  );
};

export default Thread;