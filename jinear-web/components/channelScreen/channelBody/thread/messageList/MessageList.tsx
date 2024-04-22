import React, { useEffect, useMemo, useState } from "react";
import styles from "./MessageList.module.css";
import { MessageDto, ThreadDto } from "@/be/jinear-core";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import { checkAndUpdateThreadLastMessage, selectThreadLastMessage } from "@/slice/messagingSlice";
import ThreadMessage from "@/components/channelScreen/channelBody/thread/threadMessage/ThreadMessage";
import useTranslation from "@/locals/useTranslation";
import Button, { ButtonHeight } from "@/components/button";
import { useLazyRetrieveThreadMessagesQuery } from "@/api/messageListingApi";
import Logger from "@/utils/logger";
import CircularLoading from "@/components/circularLoading/CircularLoading";

interface MessageListProps {
  thread: ThreadDto;
  workspaceName: string;
  viewingAsDetail: boolean;
}

interface MessageMap {
  [messageId: string]: MessageDto;
}

const logger = Logger("MessageList");

const MessageList: React.FC<MessageListProps> = ({ thread, workspaceName, viewingAsDetail }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const remainingMessageCount = thread.threadMessageInfo.messageCount - (thread.threadMessageInfo.initialMessage.messageId == thread.threadMessageInfo.latestMessage.messageId ? 1 : 2);
  const threadLastMessage = useTypedSelector(selectThreadLastMessage(thread.threadId));
  const initialMessageId = threadLastMessage?.messageId != thread.threadMessageInfo.initialMessage.messageId;

  const [retrieveThreadMessages, {
    data: retrieveThreadMessagesResponse,
    isLoading: isRetrieveThreadMessagesLoading,
    isFetching: isRetrieveThreadMessagesFetching
  }] = useLazyRetrieveThreadMessagesQuery();
  const [hasMore, setHasMore] = useState<boolean>(true);
  const [messageMap, setMessageMap] = useState<MessageMap>({});
  const sortedMapValues = useMemo(() => {
    return Object.values(messageMap).sort((a, b) => new Date(a.createdDate).getTime() - new Date(b.createdDate).getTime());
  }, [JSON.stringify(messageMap)]);


  useEffect(() => {
    if (thread) {
      dispatch(checkAndUpdateThreadLastMessage({ message: thread.threadMessageInfo.latestMessage }));
    }
  }, [dispatch, thread]);

  useEffect(() => {
    if (viewingAsDetail && thread) {
      retrieveThreadMessages({ threadId: thread.threadId });
    }
  }, [thread, viewingAsDetail, retrieveThreadMessages]);

  useEffect(() => {
    if (retrieveThreadMessagesResponse?.data) {
      setMessageMap(curr => {
        const currentClone = { ...curr };
        const retrievedContent = retrieveThreadMessagesResponse.data.content || [];
        retrievedContent.forEach(val => currentClone[val.messageId] = val);
        logger.log({ curr, currentClone });
        return currentClone;
      });
      setHasMore(retrieveThreadMessagesResponse.data.hasNext);
    }
  }, [retrieveThreadMessagesResponse]);

  useEffect(() => {
    if (threadLastMessage) {
      setMessageMap(curr => {
        const currentClone = { ...curr };
        currentClone[threadLastMessage.messageId] = threadLastMessage;
        logger.log({ threadLastMessage, curr, currentClone });
        return currentClone;
      });
    }
  }, [threadLastMessage]);

  const loadMore = () => {
    const oldestMessage = sortedMapValues[0];
    if (oldestMessage) {
      retrieveThreadMessages({ threadId: thread.threadId, before: new Date(oldestMessage.createdDate) });
    }
  };

  return (
    <div className={styles.container}>
      {remainingMessageCount != 0 && !viewingAsDetail &&
        <Button
          href={`/${workspaceName}/conversations/channel/${thread.channelId}/thread/${thread.threadId}`}
          heightVariant={ButtonHeight.short}
          className={styles.seeMoreRepliesButton}
        >
          {t("threadMessageRemainingMessageCount").replace("{count}", `${remainingMessageCount}`)}
        </Button>}
      {hasMore && viewingAsDetail &&
        <Button
          heightVariant={ButtonHeight.short}
          className={styles.seeMoreRepliesButton}
          onClick={loadMore}
          disabled={isRetrieveThreadMessagesFetching}
        >
          {t("threadMessageListLoadMore")}
        </Button>
      }
      {isRetrieveThreadMessagesFetching && <CircularLoading />}
      {sortedMapValues.map(message => <ThreadMessage key={message.messageId} threadMessage={message} />)}
    </div>
  );
};

export default MessageList;