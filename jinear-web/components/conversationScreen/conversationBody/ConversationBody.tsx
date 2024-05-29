import React, { useEffect, useRef } from "react";
import styles from "./ConversationBody.module.css";
import { useLazyRetrieveConversationMessagesQuery } from "@/api/messageListingApi";
import Button, { ButtonHeight } from "@/components/button";
import CircularLoading from "@/components/circularLoading/CircularLoading";
import useTranslation from "@/locals/useTranslation";
import Message from "@/components/conversationScreen/conversationBody/message/Message";
import { differenceInMinutes } from "date-fns";
import { useAppDispatch } from "@/store/store";
import { checkAndUpdateConversationLastCheck } from "@/slice/messagingSlice";
import { useConversationMessagesSorted } from "@/hooks/messaging/conversationMessage/useConversationMessagesSorted";
import { useConversationHasMoreMessages } from "@/hooks/messaging/conversation/useConversationHasMoreMessages";
import Logger from "@/utils/logger";
import { usePageVisibility } from "@/hooks/usePageVisibility";
import { decideAndScrollToBottom } from "@/utils/htmlUtils";
import { useLiveQuery } from "dexie-react-hooks";
import { getConversationMessages } from "../../../repository/MessageRepository";

interface ConversationBodyProps {
  conversationId: string,
  workspaceId: string,
}

const RENDER_MESSAGE_PROFILE_PIC_FROM_SAME_ACC_AFTER_MIN = 5;
const logger = Logger("ConversationBody");

const ConversationBody: React.FC<ConversationBodyProps> = ({ conversationId, workspaceId }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const pageVisibility = usePageVisibility();
  const [retrieveConversationMessages, {
    isLoading: isRetrieveConversationLoading,
    isFetching: isRetrieveConversationFetching
  }] = useLazyRetrieveConversationMessagesQuery();

  const hasMore = useConversationHasMoreMessages({ conversationId, workspaceId });
  // const messages = useConversationMessagesSorted({ workspaceId, conversationId });
  const messages = useLiveQuery(() => getConversationMessages(conversationId));
  const initialScroll = useRef<boolean>(false);

  logger.log({ sortedMessages: messages });

  useEffect(() => {
    dispatch(checkAndUpdateConversationLastCheck({ workspaceId, conversationId, lastCheckDate: new Date() }));
  }, [dispatch, workspaceId, conversationId, messages]);

  useEffect(() => {
    if (pageVisibility) {
      retrieveConversationMessages({ workspaceId, conversationId });
    }
  }, [retrieveConversationMessages, workspaceId, conversationId, pageVisibility]);

  useEffect(() => {
    if (messages && messages?.length > 1 && typeof window === "object") {
      decideAndScrollToBottom({
        initialShouldScroll: !initialScroll.current,
        callBack: () => {
          initialScroll.current = true;
        }
      });
    }
  }, [messages]);

  const retrieveMore = () => {
    const oldestMessage = messages ? messages[messages.length - 1] : null;
    if (oldestMessage) {
      retrieveConversationMessages({ workspaceId, conversationId, before: new Date(oldestMessage.createdDate) });
    }
  };

  return (
    <div className={styles.container}>
      {hasMore &&
        <Button
          onClick={retrieveMore}
          heightVariant={ButtonHeight.short}
          disabled={isRetrieveConversationFetching}
          loading={isRetrieveConversationFetching}
        >
          {t("conversationLoadMore")}
        </Button>
      }
      <div className={styles.loadingContainer}>
        {isRetrieveConversationLoading && !hasMore && <CircularLoading />}
      </div>
      <div className={styles.contentContainer}>
        {messages && messages?.length == 0 && !isRetrieveConversationLoading &&
          <div className={styles.emptyStateContainer}>{t("conversationEmpty")}</div>}
        <div className={styles.messageListContainer}>
          {messages?.map?.((messageDto, index) => {
              const oneBefore = messages?.[index + 1];
              const differenceInMin = oneBefore ? Math.abs(differenceInMinutes(new Date(oneBefore.createdDate), new Date(messageDto.createdDate))) : 999;
              const oneBeforeIsFromDifferentSender = messageDto.accountId != oneBefore?.accountId;
              const oneBeforeSameSenderAndOlderThanThreshold = !oneBeforeIsFromDifferentSender && differenceInMin > RENDER_MESSAGE_PROFILE_PIC_FROM_SAME_ACC_AFTER_MIN;
              return (
                <Message
                  key={messageDto.messageId}
                  message={messageDto}
                  renderMessageFrom={oneBeforeIsFromDifferentSender || oneBeforeSameSenderAndOlderThanThreshold}
                />
              );
            }
          )}
        </div>
      </div>
    </div>
  );
};

export default ConversationBody;