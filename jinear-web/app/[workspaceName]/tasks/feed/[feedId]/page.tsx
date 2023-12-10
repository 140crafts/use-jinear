"use client";
import CircularLoading from "@/components/circularLoading/CircularLoading";
import EndlessScrollList from "@/components/endlessScrollList/EndlessScrollList";
import FeedInfo from "@/components/feedPage/feedInfo/FeedInfo";
import FeedListItem from "@/components/feedPage/feedListItem/FeedListItem";
import { FeedContentItemDto } from "@/model/be/jinear-core";
import { useLazyRetrieveFeedContentQuery } from "@/store/api/feedContentApi";
import { selectWorkspaceFromWorkspaceUsername } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import Logger from "@/utils/logger";
import { isSameDay } from "date-fns";
import useTranslation from "locales/useTranslation";
import { useParams } from "next/navigation";
import React, { useEffect, useState } from "react";
import styles from "./page.module.css";

interface FeedContentPageProps {}

const logger = Logger("FeedContentPage");
const FeedContentPage: React.FC<FeedContentPageProps> = ({}) => {
  const { t } = useTranslation();
  const params = useParams();
  const workspaceName: string = params?.workspaceName as string;
  const feedId: string = params?.feedId as string;

  const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));
  const [feedData, setFeedData] = useState<FeedContentItemDto[]>([]);

  const [retrieveFeedContent, { data: retrieveFeedContentResponse, isFetching, isLoading }] = useLazyRetrieveFeedContentQuery();
  const feed = retrieveFeedContentResponse?.data.feed;

  useEffect(() => {
    if (feedId && workspace) {
      const workspaceId = workspace.workspaceId;
      retrieveFeedContent({ workspaceId, feedId });
    }
  }, [feedId, workspace]);

  useEffect(() => {
    if (retrieveFeedContentResponse) {
      const dataset = [...feedData];
      const newItems = retrieveFeedContentResponse.data.feedItemList.filter(
        (feedItem) => dataset.findIndex((existingItem) => existingItem.externalId == feedItem.externalId) == -1
      );
      setFeedData((feedData) => [...feedData, ...newItems]);
    }
  }, [retrieveFeedContentResponse]);

  const renderContentItem = (data: FeedContentItemDto, index: number) => {
    if (!workspace) {
      return null;
    }
    const oneBefore = index == 0 ? null : feedData?.[index - 1];
    const datesEqual = oneBefore && oneBefore.date && data.date && isSameDay(new Date(oneBefore.date), new Date(data.date));
    return <FeedListItem key={data.externalId} data={data} withDateTitle={!datesEqual} workspaceId={workspace.workspaceId} />;
  };

  const loadMore = () => {
    if (retrieveFeedContentResponse && feedId && workspace) {
      const workspaceId = workspace.workspaceId;
      const nextPageToken = retrieveFeedContentResponse.data.nextPageToken;
      retrieveFeedContent({ workspaceId, feedId, pageToken: nextPageToken });
    }
  };

  return (
    <div className={styles.container}>
      {isFetching && !feed && <CircularLoading />}
      {feed && <FeedInfo feed={feed} />}
      {feed && (
        <EndlessScrollList
          id={`endless-scroll-feed-${feedId}`}
          data={feedData}
          isFetching={isFetching}
          renderItem={renderContentItem}
          emptyLabel={t("feedPageEmptyLabel")}
          hasMore={retrieveFeedContentResponse?.data?.nextPageToken != null}
          loadMore={loadMore}
          loadMoreLabel={t("feedPageLoadMoreLabel")}
          loadMoreLoading={isFetching}
        />
      )}
    </div>
  );
};

export default FeedContentPage;
