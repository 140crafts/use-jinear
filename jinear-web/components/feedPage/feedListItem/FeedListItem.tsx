"use client";
import Button from "@/components/button";
import useWindowSize from "@/hooks/useWindowSize";
import { FeedContentItemDto, WorkspaceDto } from "@/model/be/jinear-core";
import { popIntegrationFeedItemDetailModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import Logger from "@/utils/logger";
import { format, isToday, isYesterday } from "date-fns";
import useTranslation from "locales/useTranslation";
import React, { useMemo } from "react";
import styles from "./FeedListItem.module.css";
import FeedListItemParticipant from "./feedListItemParticipant/FeedListItemParticipant";
const logger = Logger("FeedListItem");

interface FeedListItemProps {
  data: FeedContentItemDto;
  withDateTitle?: boolean;
  workspace: WorkspaceDto;
}

const FeedListItem: React.FC<FeedListItemProps> = ({ workspace, withDateTitle, data }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const { isMobile } = useWindowSize();

  const dateLabel = useMemo(() => {
    const currentItemDay = data.date;
    if (withDateTitle && currentItemDay) {
      const _curr = new Date(currentItemDay);
      if (isToday(_curr)) {
        return t("feedItemLabelToday");
      }
      if (isYesterday(_curr)) {
        return t("feedItemLabelYesterday");
      }
      return format(_curr, t("dateFormat"));
    }
    return;
  }, [withDateTitle, data]);

  const href = `${data.feed.feedId}/item/${data.externalId}`;

  const onLinkClick = (event: React.MouseEvent<HTMLAnchorElement> | undefined) => {
    event?.preventDefault();
    popModalView();
  };

  const popModalView = () => {
    const feedId = data.feed.feedId;
    const itemId = data.externalId;
    const title = data.title;
    feedId && itemId && dispatch(popIntegrationFeedItemDetailModal({ visible: true, workspace, feedId, itemId, title }));
  };

  return (
    <>
      {dateLabel && withDateTitle && <h1 className={styles.dateHeader}>{dateLabel}</h1>}
      <Button href={href} className={styles.container} onClick={onLinkClick}>
        <div className={styles.headerContainer}></div>
        <div className={styles.contentContainer}>
          <div className={styles.title}>{data.title}</div>
          <div className={styles.body} dangerouslySetInnerHTML={{ __html: data.text || "" }}></div>
        </div>
        <div className={styles.footerContainer}>
          <div className={styles.date}>{data.date && format(new Date(data?.date), t("dateTimeFormat"))}</div>
          <div className={styles.participantsContainer}>
            {data.participants?.map((participant, index) => (
              <FeedListItemParticipant key={data.externalId + participant.address} participant={participant} index={index} />
            ))}
          </div>
        </div>
      </Button>
    </>
  );
};

export default FeedListItem;
