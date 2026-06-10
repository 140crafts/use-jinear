import Button, { ButtonVariants } from "@/components/button";
import { FeedContentItemDto, WorkspaceDto } from "@/model/be/jinear-core";
import { popIntegrationFeedItemDetailModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import React from "react";
import { LuMailCheck } from "react-icons/lu";
import styles from "./FeedContentItem.module.css";

interface FeedContentItemProps {
  workspace?: WorkspaceDto | null;
  feedContentItem: FeedContentItemDto;
}

const FeedContentItem: React.FC<FeedContentItemProps> = ({ workspace, feedContentItem }) => {
  const dispatch = useAppDispatch();
  const popDetail = () => {
    workspace &&
      dispatch(
        popIntegrationFeedItemDetailModal({
          visible: true,
          workspace,
          feedId: feedContentItem.feed.feedId,
          itemId: feedContentItem.externalId || "",
        })
      );
  };

  return (
    <Button onClick={popDetail} className={styles.container} variant={ButtonVariants.filled}>
      <LuMailCheck />
      <div className="line-clamp">{feedContentItem?.messages?.[0]?.subject}</div>
    </Button>
  );
};

export default FeedContentItem;
