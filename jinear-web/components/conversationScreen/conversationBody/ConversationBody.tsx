import React, { useEffect, useRef, useState } from "react";
import styles from "./ConversationBody.module.css";
import { useLazyRetrieveConversationMessagesQuery } from "@/api/messageListingApi";
import Button, { ButtonHeight } from "@/components/button";
import CircularLoading from "@/components/circularLoading/CircularLoading";
import useTranslation from "@/locals/useTranslation";
import Message from "@/components/conversationScreen/conversationBody/message/Message";
import { differenceInMinutes } from "date-fns";
import { useAppDispatch } from "@/store/store";
import { checkAndUpdateConversationLastCheck, upsertAllConversationMessages } from "@/slice/messagingSlice";
import { useConversationMessagesSorted } from "@/hooks/messaging/conversationMessage/useConversationMessagesSorted";
import { useConversationHasMoreMessages } from "@/hooks/messaging/conversation/useConversationHasMoreMessages";

interface ConversationBodyProps {
  conversationId: string,
  workspaceId: string,
}

const RENDER_MESSAGE_PROFILE_PIC_FROM_SAME_ACC_AFTER_MIN = 5;

const ConversationBody: React.FC<ConversationBodyProps> = ({ conversationId, workspaceId }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const [retrieveConversationMessages, {
    data: retrieveConversationMessagesResponse,
    isFetching: isRetrieveConversationFetching
  }] = useLazyRetrieveConversationMessagesQuery();

  const hasMore = useConversationHasMoreMessages({ conversationId, workspaceId });
  const initialScroll = useRef<boolean>(false);
  const sortedMessages = useConversationMessagesSorted({ workspaceId, conversationId });

  useEffect(() => {
    dispatch(checkAndUpdateConversationLastCheck({ workspaceId, conversationId, lastCheckDate: new Date() }));
  }, [dispatch, workspaceId, conversationId]);

  useEffect(() => {
    retrieveConversationMessages({ workspaceId, conversationId });
  }, [retrieveConversationMessages, workspaceId, conversationId]);

  // useEffect(() => {
  //   if (retrieveConversationMessagesResponse?.data) {
  //     const retrievedContent = retrieveConversationMessagesResponse.data.content || [];
  //     dispatch(upsertAllConversationMessages({ workspaceId, messageDtoList: retrievedContent }));
  //   }
  // }, [dispatch, workspaceId, retrieveConversationMessagesResponse]);

  useEffect(() => {
    if (retrieveConversationMessagesResponse && typeof window === "object" && !initialScroll.current) {
      setTimeout(() => {
        window.scrollTo({
          top: document.documentElement.scrollHeight - window.innerHeight,
          left: 0,
          behavior: "auto"
        });
        initialScroll.current = true;
      }, 500);
    }
  }, [retrieveConversationMessagesResponse]);

  const retrieveMore = () => {
    const oldestMessage = sortedMessages[sortedMessages.length - 1];
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
      {isRetrieveConversationFetching && !hasMore && <CircularLoading />}
      <div className={styles.contentContainer}>
        {sortedMessages?.length == 0 && !isRetrieveConversationFetching &&
          <div className={styles.emptyStateContainer}>{t("conversationEmpty")}</div>}
        <div className={styles.messageListContainer}>
          {sortedMessages?.map?.((messageDto, index) => {
              const oneBefore = sortedMessages?.[index - 1];
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