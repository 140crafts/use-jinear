import { useRetrieveTaskActivityQuery } from "@/store/api/taskActivityApi";
import { CircularProgress } from "@mui/material";
import useTranslation from "locales/useTranslation";
import React from "react";
import TaskActivity from "./taskActivity/TaskActivity";
import styles from "./TaskActivityList.module.css";

interface TaskActivityListProps {
  taskId: string;
}

const TaskActivityList: React.FC<TaskActivityListProps> = ({ taskId }) => {
  const { t } = useTranslation();
  const {
    data: retrieveTaskActivityResponse,
    isSuccess,
    isLoading,
    isFetching,
  } = useRetrieveTaskActivityQuery({ taskId }, { skip: taskId == null });

  return (
    <div className={styles.container}>
      <h3>{t("taskActivityListTitle")}</h3>
      <div className="spacer-h-2" />
      {(isFetching || isLoading) && (
        <div className={styles.loading}>
          <CircularProgress size={12} />
        </div>
      )}
      {isSuccess &&
        retrieveTaskActivityResponse.data.map((activity) => (
          <TaskActivity key={activity.workspaceActivityId} activity={activity} />
        ))}
    </div>
  );
};

export default TaskActivityList;
