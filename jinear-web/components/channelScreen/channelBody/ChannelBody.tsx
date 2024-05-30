import React, { useEffect, useRef, useState } from "react";
import styles from "./ChannelBody.module.css";
import { useLazyListThreadsQuery } from "@/api/threadApi";
import Logger from "@/utils/logger";
import Thread from "@/components/channelScreen/channelBody/thread/Thread";
import CircularLoading from "@/components/circularLoading/CircularLoading";
import Button, { ButtonHeight } from "@/components/button";
import useTranslation from "@/locals/useTranslation";
import { checkAndUpdateChannelLastCheck } from "@/slice/messagingSlice";
import { useAppDispatch } from "@/store/store";
import { usePageVisibility } from "@/hooks/usePageVisibility";
import { useLiveQuery } from "dexie-react-hooks";
import { getThreadsWithMessages } from "../../../repository/IndexedDbRepository";
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
  const dispatch = useAppDispatch();
  const pageVisibility = usePageVisibility();
  const [listThreads, { data: listThreadsResponse, isFetching: isListThreadsFetching }] = useLazyListThreadsQuery();
  const [hasMore, setHasMore] = useState<boolean>(true);

  const initialScroll = useRef<boolean>(false);
  const threads = useLiveQuery(() => getThreadsWithMessages(channelId)) || [];

  useEffect(() => {
    if (workspaceId && channelId && pageVisibility) {
      dispatch(checkAndUpdateChannelLastCheck({ workspaceId, channelId, lastCheckDate: new Date() }));
    }
  }, [dispatch, workspaceId, channelId, pageVisibility]);

  useEffect(() => {
    if (workspaceId && channelId && pageVisibility) {
      listThreads({ workspaceId, channelId });
    }
  }, [dispatch, listThreads, workspaceId, channelId, pageVisibility]);

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
        {threads?.length == 0 && !isListThreadsFetching &&
          <div className={styles.emptyStateContainer}>{t("threadsEmpty")}</div>}
        {threads?.map?.(threadDto =>
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