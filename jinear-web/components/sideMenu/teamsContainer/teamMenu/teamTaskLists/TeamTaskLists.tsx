import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import MenuGroupTitle from "@/components/sideMenu/menuGroupTitle/MenuGroupTitle";
import { useRetrieveAllTaskListsQuery } from "@/store/api/TaskListListingApi";
import { popNewTaskListModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import { CircularProgress } from "@mui/material";
import useTranslation from "locales/useTranslation";
import { useRouter } from "next/router";
import React from "react";
import styles from "./TeamTaskLists.module.css";
import TaskListRow from "./taskListRow/TaskListRow";

interface TeamTaskListsProps {
  teamId: string;
  workspaceId: string;
  workspaceUsername: string;
  teamUsername: string;
}

const VISIBLE_SIZE = 3;

const TeamTaskLists: React.FC<TeamTaskListsProps> = ({ teamId, workspaceId, workspaceUsername, teamUsername }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const router = useRouter();

  const { data: taskListListingResponse, isFetching } = useRetrieveAllTaskListsQuery({ teamId, workspaceId });
  const hasMore = taskListListingResponse?.data.totalElements && taskListListingResponse?.data.totalElements > VISIBLE_SIZE;
  const notVisibleSize = hasMore ? taskListListingResponse?.data.totalElements - VISIBLE_SIZE : 0;

  const openNewTaskListModal = () => {
    dispatch(popNewTaskListModal());
  };

  const routeToTaskListScreen = () => {
    router.push(`/${workspaceUsername}/${teamUsername}/task-lists`);
  };

  return (
    <div className={styles.container}>
      <MenuGroupTitle
        label={t("sideMenuTeamTaskLists")}
        hasDetailButton={true}
        hasAddButton={true}
        onAddButtonClick={openNewTaskListModal}
        onDetailButtonClick={routeToTaskListScreen}
      />
      <div className={styles.listsContainer}>
        {isFetching && <CircularProgress size={11} />}
        {!taskListListingResponse?.data?.hasContent && !isFetching && (
          <div className={styles.noContentLabel}>{t("sideMenuTaskListsNoTaskListExists")}</div>
        )}
        {taskListListingResponse?.data?.content?.slice(0, VISIBLE_SIZE).map((taskListDto) => (
          <TaskListRow key={taskListDto.taskListId} taskListDto={taskListDto} />
        ))}
        {hasMore && (
          <Button
            variant={ButtonVariants.hoverFilled}
            className={styles.hasMoreButton}
            heightVariant={ButtonHeight.short}
            href={`/${workspaceUsername}/${teamUsername}/task-lists`}
          >
            {t("sideMenuTeamTaskListsShowMore").replace("${number}", `${notVisibleSize}`)}
          </Button>
        )}
      </div>
    </div>
  );
};

export default TeamTaskLists;
