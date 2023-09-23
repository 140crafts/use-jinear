import TaskDetailBreadcrumb from "@/components/taskDetailBreadcrumb/TaskDetailBreadcrumb";
import { TaskDto } from "@/model/be/jinear-core";
import React from "react";
import styles from "./TaskDetailHeader.module.css";

interface TaskDetailHeaderProps {
  task: TaskDto;
  backButtonVisible?: boolean;
}

const TaskDetailHeader: React.FC<TaskDetailHeaderProps> = ({ task, backButtonVisible = false }) => {
  return (
    <div className={styles.headerContainer}>
      <TaskDetailBreadcrumb task={task} />
    </div>
  );
};

export default TaskDetailHeader;
