import MenuGroupTitle from "@/components/sideMenu/menuGroupTitle/MenuGroupTitle";
import { useRetrieveAllTaskListsQuery } from "@/store/api/TaskListListingApi";
import { popNewTaskListModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import { CircularProgress } from "@mui/material";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./TeamTaskLists.module.css";
import TaskListRow from "./taskListRow/TaskListRow";

interface TeamTaskListsProps {
  teamId: string;
  workspaceId: string;
}

const TeamTaskLists: React.FC<TeamTaskListsProps> = ({ teamId, workspaceId }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const { data: taskListListingResponse, isFetching } = useRetrieveAllTaskListsQuery({ teamId, workspaceId });

  const openNewTaskListModal = () => {
    dispatch(popNewTaskListModal());
  };

  return (
    <div className={styles.container}>
      <MenuGroupTitle label={t("sideMenuTeamTaskLists")} hasAddButton={true} onAddButtonClick={openNewTaskListModal} />
      <div className={styles.listsContainer}>
        {isFetching && <CircularProgress size={11} />}
        {!taskListListingResponse?.data?.hasContent && !isFetching && (
          <div className={styles.noContentLabel}>{t("sideMenuTaskListsNoTaskListExists")}</div>
        )}
        {taskListListingResponse?.data?.content?.map((taskListDto) => (
          <TaskListRow key={taskListDto.taskListId} taskListDto={taskListDto} />
        ))}
      </div>
    </div>
  );
};

export default TeamTaskLists;
