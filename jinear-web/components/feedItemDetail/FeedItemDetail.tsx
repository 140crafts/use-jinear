import { useRetrieveFeedContentItemQuery } from "@/store/api/feedContentApi";
import { getOffset } from "@/utils/htmlUtis";
import Logger from "@/utils/logger";
import useTranslation from "locales/useTranslation";
import React, { useEffect } from "react";
import Button, { ButtonHeight, ButtonVariants } from "../button";
import CircularLoading from "../circularLoading/CircularLoading";
import styles from "./FeedItemDetail.module.css";
import FeedItemMessage from "./feedItemMessage/FeedItemMessage";

interface FeedItemDetailProps {
  workspaceId: string;
  feedId: string;
  itemId: string;
}
const logger = Logger("FeedItemDetail");

const FeedItemDetail: React.FC<FeedItemDetailProps> = ({ workspaceId, feedId, itemId }) => {
  const { t } = useTranslation();
  const { data: feedContentItemResponse, isFetching } = useRetrieveFeedContentItemQuery({ workspaceId, feedId, itemId });
  useEffect(() => {
    if (feedContentItemResponse) {
      setTimeout(() => {
        const lastMessageElement = document.getElementById("feed-last-message");
        logger.log({ lastMessageElement });
        if (lastMessageElement) {
          const offset = getOffset(lastMessageElement);
          scroll?.({
            top: offset.top,
            behavior: "smooth",
          });
        }
      }, 250);
    }
  }, [feedContentItemResponse]);
  return (
    <div className={styles.container}>
      {isFetching && <CircularLoading />}
      {feedContentItemResponse?.data.messages?.map((message, index) => (
        <FeedItemMessage
          key={message.externalGroupId + "" + message.externalId}
          message={message}
          isLast={feedContentItemResponse?.data.messages?.length == index + 1}
        />
      ))}
      {feedContentItemResponse && (
        <div className={styles.actionButtonContainer}>
          <Button variant={ButtonVariants.contrast} heightVariant={ButtonHeight.short}>
            {t("feedItemDetailCreateTask")}
          </Button>
        </div>
      )}
    </div>
  );
};

export default FeedItemDetail;
