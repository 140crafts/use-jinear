import { useRetrieveTaskActivityQuery } from "@/store/api/taskActivityApi";
import { CircularProgress } from "@mui/material";
import React from "react";
import TaskActivity from "./taskActivity/TaskActivity";
import styles from "./TaskActivityList.module.css";

interface TaskActivityListProps {
  taskId: string;
}

const TaskActivityList: React.FC<TaskActivityListProps> = ({ taskId }) => {
  const {
    data: retrieveTaskActivityResponse,
    isSuccess,
    isLoading,
    isFetching,
  } = useRetrieveTaskActivityQuery({ taskId }, { skip: taskId == null });

  return (
    <div className={styles.container}>
      {isSuccess &&
        retrieveTaskActivityResponse.data.map((activity) => (
          <TaskActivity
            key={activity.workspaceActivityId}
            activity={activity}
          />
        ))}

      {(isFetching || isLoading) && (
        <div className={styles.loading}>
          <CircularProgress size={17} />
        </div>
      )}
    </div>
  );
};

export default TaskActivityList;
