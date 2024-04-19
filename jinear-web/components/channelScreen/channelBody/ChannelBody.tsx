import React, { useEffect, useMemo, useRef, useState } from "react";
import styles from "./ChannelBody.module.css";
import { useLazyListThreadsQuery } from "@/api/threadApi";
import { ThreadDto } from "@/be/jinear-core";
import Logger from "@/utils/logger";

interface ChannelBodyProps {
  workspaceName: string;
  channelId: string;
  workspaceId: string;
}

interface ThreadMap {
  [threadId: string]: ThreadDto;
}

const logger = Logger("ChannelBody");

const ChannelBody: React.FC<ChannelBodyProps> = ({ workspaceName, channelId, workspaceId }) => {
  const [listThreads, { data: listThreadsResponse, isLoading: isListThreadsLoading }] = useLazyListThreadsQuery();
  const [hasMore, setHasMore] = useState<boolean>(true);
  const initialScroll = useRef<boolean>(false);

  const [threadMap, setThreadMap] = useState<ThreadMap>({});
  const sortedMapValues = useMemo(() => {
    return Object.values(threadMap).sort((a, b) => new Date(a.lastActivityTime).getTime() - new Date(b.lastActivityTime).getTime());
  }, [JSON.stringify(threadMap)]);

  logger.log({ threadMap, sortedMapValues, mapKeyLength: Object.keys(threadMap).length });

  useEffect(() => {
    listThreads({ channelId });
  }, [listThreads, channelId]);

  useEffect(() => {
    if (listThreadsResponse && listThreadsResponse.data) {
      setThreadMap(curr => {
        const currentClone = { ...curr };
        const retrievedContent = listThreadsResponse.data.content || [];
        retrievedContent.forEach(val => currentClone[val.threadId] = val);
        logger.log({ curr, currentClone });
        return currentClone;
      });
      setHasMore(listThreadsResponse.data.hasNext);

      if (typeof window === "object" && !initialScroll.current) {
        window.scrollTo({
          top: document.documentElement.scrollHeight - window.innerHeight,
          left: 0,
          behavior: "auto"
        });
        initialScroll.current = true;
      }

    }
  }, [initialScroll, listThreadsResponse]);

  const retrieveMore = () => {

  };

  return (
    <div className={styles.container}>
      {threadMap && Object.values(threadMap).map(threadDto => (
        <div key={threadDto.threadId + threadDto.lastActivityTime}>
          {new Date(threadDto.lastActivityTime).toISOString()}
          {threadDto.threadMessageInfo.initialMessage.richText.value}
          {threadDto.threadMessageInfo.latestMessage.richText.value}
        </div>
      ))}
    </div>
  );
};

export default ChannelBody;