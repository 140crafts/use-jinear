import Button, { ButtonVariants } from "@/components/button";
import TaskDetailBreadcrumb from "@/components/taskDetailBreadcrumb/TaskDetailBreadcrumb";
import { TaskDto } from "@/model/be/jinear-core";
import { HOST } from "@/utils/constants";
import { useRouter } from "next/router";
import React from "react";
import styles from "./TaskDetailHeader.module.css";

interface TaskDetailHeaderProps {
  task: TaskDto;
  backButtonVisible?: boolean;
}

const TaskDetailHeader: React.FC<TaskDetailHeaderProps> = ({ task, backButtonVisible = false }) => {
  const router = useRouter();

  const goBack = () => {
    router.back?.();
  };

  return (
    <div className={styles.headerContainer}>
      {task.workspace?.isPersonal ? (
        backButtonVisible &&
        document.referrer?.indexOf(HOST) != -1 && (
          <Button onClick={goBack} variant={ButtonVariants.filled}>
            <b>{"<-"}</b>
          </Button>
        )
      ) : (
        <TaskDetailBreadcrumb task={task} />
      )}
    </div>
  );
};

export default TaskDetailHeader;
