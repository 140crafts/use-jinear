import React, { useEffect, useMemo, useRef, useState } from "react";
import styles from "./ChannelBody.module.css";
import { useLazyListThreadsQuery } from "@/api/threadApi";
import { ThreadDto } from "@/be/jinear-core";
import Logger from "@/utils/logger";
import Thread from "@/components/channelScreen/channelBody/thread/Thread";
import CircularLoading from "@/components/circularLoading/CircularLoading";
import Button, { ButtonHeight } from "@/components/button";
import useTranslation from "@/locals/useTranslation";
import { checkAndUpdateChannelLastCheck } from "@/slice/messagingSlice";
import { useAppDispatch } from "@/store/store";
import { usePageVisibility } from "@/hooks/usePageVisibility";

interface ChannelBodyProps {
  workspaceName: string;
  channelId: string;
  workspaceId: string;
  canReplyThreads: boolean;
}

interface ThreadMap {
  [threadId: string]: ThreadDto;
}

const logger = Logger("ChannelBody");

const ChannelBody: React.FC<ChannelBodyProps> = ({ channelId, canReplyThreads, workspaceId, workspaceName }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const pageVisibility = usePageVisibility();
  const [listThreads, { data: listThreadsResponse, isFetching: isListThreadsFetching }] = useLazyListThreadsQuery();
  const [hasMore, setHasMore] = useState<boolean>(true);
  const initialScroll = useRef<boolean>(false);

  const [threadMap, setThreadMap] = useState<ThreadMap>({});
  const sortedMapValues = useMemo(() => {
    return Object.values(threadMap).sort((a, b) => new Date(b.lastActivityTime).getTime() - new Date(a.lastActivityTime).getTime());
  }, [JSON.stringify(threadMap)]);

  logger.log({ threadMap, sortedMapValues, mapKeyLength: Object.keys(threadMap).length });

  useEffect(() => {
    if (workspaceId && channelId && pageVisibility) {
      dispatch(checkAndUpdateChannelLastCheck({ workspaceId, channelId, lastCheckDate: new Date() }));
    }
  }, [dispatch, workspaceId, channelId, pageVisibility]);

  useEffect(() => {
    if (workspaceId && channelId && pageVisibility) {
      listThreads({ workspaceId, channelId });
      dispatch(checkAndUpdateChannelLastCheck({ workspaceId, channelId, lastCheckDate: new Date() }));
    }
  }, [dispatch, listThreads, workspaceId, channelId, pageVisibility]);

  useEffect(() => {
    if (listThreadsResponse?.data) {
      setThreadMap(curr => {
        const currentClone = { ...curr };
        const retrievedContent = listThreadsResponse.data.content || [];
        retrievedContent.forEach(val => currentClone[val.threadId] = val);
        logger.log({ curr, currentClone });
        return currentClone;
      });
      setHasMore(listThreadsResponse.data.hasNext);
    }
  }, [listThreadsResponse]);

  useEffect(() => {
    if (listThreadsResponse && typeof window === "object" && !initialScroll.current) {
      setTimeout(() => {
        window.scrollTo({
          top: document.documentElement.scrollHeight - window.innerHeight,
          left: 0,
          behavior: "auto"
        });
        initialScroll.current = true;
      }, 500);
    }
  }, [listThreadsResponse]);

  const retrieveMore = () => {
    const oldestThread = sortedMapValues[sortedMapValues.length - 1];
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
        {sortedMapValues?.length == 0 && !isListThreadsFetching &&
          <div className={styles.emptyStateContainer}>{t("threadsEmpty")}</div>}
        {sortedMapValues?.map?.(threadDto =>
          <Thread
            key={`channel-overview-${threadDto.threadId}`}
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