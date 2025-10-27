import React, { useEffect, useRef, useState } from "react";
import styles from "./ChannelBody.module.css";
import { useLazyListThreadsQuery } from "@/api/threadApi";
import Logger from "@/utils/logger";
import Thread from "@/components/channelScreen/channelBody/thread/Thread";
import CircularLoading from "@/components/circularLoading/CircularLoading";
import Button, { ButtonHeight } from "@/components/button";
import useTranslation from "@/locals/useTranslation";
import { usePageVisibility } from "@/hooks/usePageVisibility";
import { useLiveQuery } from "dexie-react-hooks";
import {
  checkAndUpdateChannelLastActivity,
  checkAndUpdateChannelLastCheck,
  getThreadsWithMessages
} from "../../../repository/IndexedDbRepository";
import { decideAndScrollToBottom } from "@/utils/htmlUtils";

interface ChannelBodyProps {
  workspaceName: string;
  channelId: string;
  workspaceId: string;
  canReplyThreads: boolean;
}

const logger = Logger("ChannelBody");

const ChannelBody: React.FC<ChannelBodyProps> = ({ channelId, canReplyThreads, workspaceId, workspaceName }) => {
  const { t } = useTranslation();
  const pageVisibility = usePageVisibility();
  const [listThreads, { isFetching: isListThreadsFetching }] = useLazyListThreadsQuery();

  const initialScroll = useRef<boolean>(false);
  const threads = useLiveQuery(() => getThreadsWithMessages(channelId)) ?? [];
  const hasMore = threads?.[threads?.length - 1]?.threadType != "CHANNEL_INITIAL";
  const hasAnyOtherThanInitial = threads?.find(t=>t.threadType != "CHANNEL_INITIAL") != null;
  logger.log({ threads, hasAnyOtherThanInitial });

  useEffect(() => {
    if (channelId && pageVisibility) {
      checkAndUpdateChannelLastCheck({ workspaceId, channelId, date: new Date() });
    }
  }, [channelId, workspaceId, pageVisibility]);

  useEffect(() => {
    if (workspaceId && channelId && pageVisibility) {
      listThreads({ workspaceId, channelId });
    }
  }, [listThreads, workspaceId, channelId, pageVisibility]);

  useEffect(() => {
    if (threads && threads.length > 1 && typeof window === "object" && !initialScroll.current) {
      decideAndScrollToBottom({
        initialShouldScroll: !initialScroll.current,
        callBack: () => {
          initialScroll.current = true;
        }
      });
    }
  }, [threads]);

  const retrieveMore = () => {
    const oldestThread = threads[threads.length - 1];
    if (oldestThread) {
      listThreads({ workspaceId, channelId, before: new Date(oldestThread.lastActivityTime) });
    }
  };

  return (
    <div className={styles.container}>
      {hasMore &&
        <Button
          onClick={retrieveMore}
          heightVariant={ButtonHeight.short}
          disabled={isListThreadsFetching}
          loading={isListThreadsFetching}
        >
          {t("threadsLoadMore")}
        </Button>
      }
      {isListThreadsFetching && !hasMore && <CircularLoading />}
      <div className={styles.contentContainer}>
        {!hasAnyOtherThanInitial && !isListThreadsFetching &&
          <div className={styles.emptyStateContainer}>
            <h1>{t("threadsEmpty")}</h1>
            <span>{t("threadsEmptyText")}</span>
          </div>}
        {threads
          ?.filter(thread => thread.threadType != "CHANNEL_INITIAL")
          .map?.(threadDto =>
            <Thread
              key={`channel-overview-${threadDto.threadId}`}
              threadWithMessages={threadDto}
              threadId={threadDto.threadId}
              channelId={threadDto.channelId}
              canReplyThreads={canReplyThreads}
              workspaceName={workspaceName}
              viewingAsDetail={false}
              workspaceId={workspaceId}
            />
          )}
      </div>
    </div>
  );
};

export default ChannelBody;