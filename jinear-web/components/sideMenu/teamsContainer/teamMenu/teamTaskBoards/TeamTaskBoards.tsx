import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import MenuGroupTitle from "@/components/sideMenu/menuGroupTitle/MenuGroupTitle";
import { useRetrieveAllTaskBoardsQuery } from "@/store/api/taskBoardListingApi";
import { popNewTaskBoardModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import { CircularProgress } from "@mui/material";
import useTranslation from "locales/useTranslation";
import { useRouter } from "next/router";
import React from "react";
import styles from "./TeamTaskBoards.module.css";
import TaskBoardRow from "./taskBoardRow/TaskBoardRow";

interface TeamTaskBoardsProps {
  teamId: string;
  workspaceId: string;
  workspaceUsername: string;
  teamUsername: string;
}

const VISIBLE_SIZE = 3;

const TeamTaskBoards: React.FC<TeamTaskBoardsProps> = ({ teamId, workspaceId, workspaceUsername, teamUsername }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const router = useRouter();

  const { data: taskBoardListingResponse, isFetching } = useRetrieveAllTaskBoardsQuery({ teamId, workspaceId });
  const hasMore =
    taskBoardListingResponse?.data.totalElements != null && taskBoardListingResponse?.data.totalElements > VISIBLE_SIZE;
  const notVisibleSize = hasMore ? taskBoardListingResponse?.data.totalElements - VISIBLE_SIZE : 0;

  const openNewTaskBoardModal = () => {
    dispatch(popNewTaskBoardModal({ visible: true }));
  };

  const routeToTaskBoardsScreen = () => {
    router.push(`/${workspaceUsername}/${teamUsername}/task-boards`);
  };

  return (
    <div className={styles.container}>
      <MenuGroupTitle
        label={t("sideMenuTeamTaskBoards")}
        hasDetailButton={true}
        hasAddButton={true}
        onAddButtonClick={openNewTaskBoardModal}
        onDetailButtonClick={routeToTaskBoardsScreen}
      />
      <div className={styles.listsContainer}>
        {isFetching && <CircularProgress size={11} />}
        {!taskBoardListingResponse?.data?.hasContent && !isFetching && (
          <div className={styles.noContentLabel}>{t("sideMenuTaskBoardsNoTaskBoardExists")}</div>
        )}
        {taskBoardListingResponse?.data?.content?.slice(0, VISIBLE_SIZE).map((taskBoardDto) => (
          <TaskBoardRow
            key={taskBoardDto.taskBoardId}
            taskBoardDto={taskBoardDto}
            workspaceUsername={workspaceUsername}
            teamUsername={teamUsername}
          />
        ))}
        {hasMore && (
          <Button
            variant={ButtonVariants.hoverFilled}
            className={styles.hasMoreButton}
            heightVariant={ButtonHeight.short}
            href={`/${workspaceUsername}/${teamUsername}/task-boards`}
          >
            {t("sideMenuTeamTaskListsShowMore").replace("${number}", `${notVisibleSize}`)}
          </Button>
        )}
      </div>
    </div>
  );
};

export default TeamTaskBoards;
