import { useWorkspaceFirstTeam } from "@/hooks/useWorkspaceFirstTeam";
import { WorkspaceDto } from "@/model/be/jinear-core";
import { useRetrieveFeedContentItemQuery } from "@/store/api/feedContentApi";
import { popNewTaskModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import { getOffset } from "@/utils/htmlUtils";
import Logger from "@/utils/logger";
import useTranslation from "locales/useTranslation";
import React, { useEffect, useMemo } from "react";
import Button, { ButtonHeight, ButtonVariants } from "../button";
import CircularLoading from "../circularLoading/CircularLoading";
import styles from "./FeedItemDetail.module.css";
import FeedItemMessage from "./feedItemMessage/FeedItemMessage";

interface FeedItemDetailProps {
  workspace: WorkspaceDto;
  feedId: string;
  itemId: string;
}
const logger = Logger("FeedItemDetail");

const GMAIL_THREAD_URI = "https://mail.google.com/mail/${emailAddress}/0/#inbox/${threadId}";

const FeedItemDetail: React.FC<FeedItemDetailProps> = ({ workspace, feedId, itemId }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const workspacesFirstTeam = useWorkspaceFirstTeam(workspace.workspaceId);
  const { data: feedContentItemResponse, isFetching } = useRetrieveFeedContentItemQuery({
    workspaceId: workspace.workspaceId,
    feedId,
    itemId,
  });
  const providerUrl = useMemo(() => {
    const provider = feedContentItemResponse?.data?.feed?.provider;
    const externalId = feedContentItemResponse?.data?.externalId || "";
    if (provider == "GOOGLE") {
      const email = feedContentItemResponse?.data?.feed?.name || "me";
      return GMAIL_THREAD_URI.replace("${emailAddress}", email).replace("${threadId}", externalId);
    }
  }, [feedContentItemResponse]);

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

  const newTask = () => {
    if (feedContentItemResponse) {
      const initialRelatedFeedItemData = {
        feedId,
        feedItemId: itemId,
        itemTitle: feedContentItemResponse.data.title || feedContentItemResponse.data?.messages?.[0]?.subject || "",
        integrationProvider: feedContentItemResponse.data.feed.provider,
      };
      dispatch(popNewTaskModal({ visible: true, workspace, team: workspacesFirstTeam, initialRelatedFeedItemData }));
    }
  };

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
          {providerUrl && (
            <Button href={providerUrl} target="_blank" heightVariant={ButtonHeight.short}>
              {t(`feedItemDetailOpenInProvider_${feedContentItemResponse.data.feed.provider}`)}
            </Button>
          )}
          <Button variant={ButtonVariants.contrast} heightVariant={ButtonHeight.short} onClick={newTask}>
            {t("feedItemDetailCreateTask")}
          </Button>
        </div>
      )}
    </div>
  );
};

export default FeedItemDetail;
