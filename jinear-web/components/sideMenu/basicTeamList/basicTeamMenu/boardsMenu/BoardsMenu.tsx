import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import CircularLoading from "@/components/circularLoading/CircularLoading";
import { TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import { useRetrieveAllTaskBoardsQuery } from "@/store/api/taskBoardListingApi";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./BoardsMenu.module.css";
import BoardItemButton from "./boardItemButton/BoardItemButton";
import BoardsMenuTitle from "./boardsMenuTitle/BoardsMenuTitle";

interface BoardsMenuProps {
  workspace: WorkspaceDto;
  team: TeamDto;
}

const VISIBLE_SIZE = 4;

const BoardsMenu: React.FC<BoardsMenuProps> = ({ workspace, team }) => {
  const { t } = useTranslation();

  const { data: taskBoardListingResponse, isFetching } = useRetrieveAllTaskBoardsQuery({ ...team });
  const hasMore =
    taskBoardListingResponse?.data.totalElements != null && taskBoardListingResponse?.data.totalElements > VISIBLE_SIZE;
  const notVisibleSize = hasMore ? taskBoardListingResponse?.data.totalElements - VISIBLE_SIZE : 0;

  return (
    <div className={styles.container}>
      <BoardsMenuTitle workspace={workspace} team={team} />
      <div className={styles.list}>
        {isFetching && <CircularLoading />}
        {taskBoardListingResponse?.data?.content?.slice(0, VISIBLE_SIZE).map((taskBoardDto) => (
          <BoardItemButton
            key={`side-menu-board-list-board-${taskBoardDto.taskBoardId}`}
            workspace={workspace}
            team={team}
            taskBoardDto={taskBoardDto}
          />
        ))}
        {hasMore && (
          <Button
            variant={ButtonVariants.hoverFilled}
            className={styles.hasMoreButton}
            heightVariant={ButtonHeight.short}
            href={`/${workspace.username}/${team.username}/task-boards`}
          >
            {t("sideMenuTeamTaskListsShowMore").replace("${number}", `${notVisibleSize}`)}
          </Button>
        )}
      </div>
    </div>
  );
};

export default BoardsMenu;
