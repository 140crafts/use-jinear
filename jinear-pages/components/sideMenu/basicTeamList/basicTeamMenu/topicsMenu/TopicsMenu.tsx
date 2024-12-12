import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import CircularLoading from "@/components/circularLoading/CircularLoading";
import { TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import { useRetrieveTeamTopicsQuery } from "@/store/api/topicListingApi";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./TopicsMenu.module.css";
import TopicItemButton from "./topicItemButton/TopicItemButton";
import TopicsMenuTitle from "./topicsMenuTitle/TopicsMenuTitle";
import { useFeatureFlag } from "@/hooks/useFeatureFlag";

interface TopicsMenuProps {
  workspace: WorkspaceDto;
  team: TeamDto;
}

const VISIBLE_SIZE = 4;

const TopicsMenu: React.FC<TopicsMenuProps> = ({ workspace, team }) => {
  const { t } = useTranslation();
  const extendedSideMenuTeamActionButtonsVisible = useFeatureFlag("EXTENDED_SIDE_MENU_TEAM_ACTION_BUTTONS_VISIBLE");

  const { data: teamTopicListingResponse, isFetching } = useRetrieveTeamTopicsQuery(team.teamId);

  const hasMore =
    teamTopicListingResponse?.data.totalElements != null && teamTopicListingResponse?.data.totalElements > VISIBLE_SIZE;
  const notVisibleSize = hasMore ? teamTopicListingResponse?.data.totalElements - VISIBLE_SIZE : 0;

  return (
    <div className={styles.container}>
      <TopicsMenuTitle workspace={workspace} team={team} />
      {extendedSideMenuTeamActionButtonsVisible && <div className={styles.list}>
        {isFetching && <CircularLoading />}
        {teamTopicListingResponse?.data?.content?.slice(0, VISIBLE_SIZE).map((topicDto) => (
          <TopicItemButton
            key={`side-menu-topic-list-topic-${topicDto.topicId}`}
            workspace={workspace}
            team={team}
            topic={topicDto}
          />
        ))}
        {hasMore && (
          <Button
            variant={ButtonVariants.hoverFilled}
            className={styles.hasMoreButton}
            heightVariant={ButtonHeight.short}
            href={`/${workspace.username}/tasks/${team.username}/topic/list`}
          >
            {t("sideMenuTeamTopicListsShowMore").replace("${number}", `${notVisibleSize}`)}
          </Button>
        )}
      </div>}
    </div>
  );
};

export default TopicsMenu;
