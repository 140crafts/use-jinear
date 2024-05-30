import React, { useEffect } from "react";
import styles from "./MessageList.module.css";
import ThreadMessage from "@/components/channelScreen/channelBody/thread/threadMessage/ThreadMessage";
import useTranslation from "@/locals/useTranslation";
import Button, { ButtonHeight } from "@/components/button";
import { useLazyRetrieveThreadMessagesQuery } from "@/api/messageListingApi";
import Logger from "@/utils/logger";
import CircularLoading from "@/components/circularLoading/CircularLoading";
import { usePageVisibility } from "@/hooks/usePageVisibility";
import { useLiveQuery } from "dexie-react-hooks";
import {
  getThreadFirstReplyMessage,
  getThreadInitialMessage,
  getThreadLastMessage,
  getThreadMessageInfo,
  getThreadMessages, IMessageDto
} from "../../../../../repository/IndexedDbRepository";

interface MessageListProps {
  channelId: string;
  threadId: string;
  workspaceId: string;
  workspaceName: string;
  viewingAsDetail: boolean;
  messages?: IMessageDto[];
}

const logger = Logger("MessageList");

const MessageList: React.FC<MessageListProps> = ({
                                                   channelId,
                                                   threadId,
                                                   workspaceId,
                                                   workspaceName,
                                                   viewingAsDetail,
                                                   messages
                                                 }) => {
  const { t } = useTranslation();
  const pageVisibility = usePageVisibility();

  const initialMessage = useLiveQuery(() => getThreadInitialMessage(threadId));
  const lastMessage = useLiveQuery(() => getThreadLastMessage(threadId));
  const threadMessageInfo = useLiveQuery(() => getThreadMessageInfo(threadId));
  const threadMessagesSorted = useLiveQuery(() => getThreadMessages(threadId)) || [];
  const threadEarliestMessageAfterInitialMessage = useLiveQuery(() => getThreadFirstReplyMessage(threadId));

  const hasMoreThanOneMessage = initialMessage?.messageId != lastMessage?.messageId;
  const remainingMessageCount = threadMessageInfo ? (threadMessageInfo.messageCount - (hasMoreThanOneMessage ? 2 : 1)) : 0;
  const hasMore = threadMessageInfo ? (threadMessageInfo.messageCount > threadMessagesSorted.length) : false;
  const [retrieveThreadMessages, { isFetching: isRetrieveThreadMessagesFetching }] = useLazyRetrieveThreadMessagesQuery();

  useEffect(() => {
    if (threadId && workspaceId && pageVisibility) {
      retrieveThreadMessages({ channelId, workspaceId, threadId });
    }
  }, [threadId, channelId, workspaceId, retrieveThreadMessages, pageVisibility]);

  const loadMore = () => {
    if (threadEarliestMessageAfterInitialMessage) {
      logger.log({ threadLastMessage: threadEarliestMessageAfterInitialMessage });
      retrieveThreadMessages({
        channelId,
        workspaceId,
        threadId,
        before: new Date(threadEarliestMessageAfterInitialMessage.createdDate)
      });
    }
  };

  return (
    <div className={styles.container}>
      {remainingMessageCount > 0 && !viewingAsDetail &&
        <Button
          href={`/${workspaceName}/conversations/channel/${channelId}/thread/${threadId}`}
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
      {isRetrieveThreadMessagesFetching && viewingAsDetail && <CircularLoading />}
      <div className={styles.messageListContainer}>
        {messages?.filter(message => message.messageId != initialMessage?.messageId)
          .slice(0, viewingAsDetail ? threadMessagesSorted.length : 2)
          .map(message =>
            <ThreadMessage key={message.messageId} threadMessage={message} />
          )}
      </div>
    </div>
  );
};

export default MessageList;