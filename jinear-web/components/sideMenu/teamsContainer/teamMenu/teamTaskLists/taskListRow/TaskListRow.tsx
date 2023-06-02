import Button, { ButtonVariants } from "@/components/button";
import { TaskBoardDto } from "@/model/be/jinear-core";
import cn from "classnames";
import React from "react";
import { IoReaderOutline } from "react-icons/io5";
import styles from "./TaskListRow.module.css";

interface TaskBoardRowProps {
  taskBoardDto: TaskBoardDto;
  workspaceUsername: string;
  teamUsername: string;
}

const TaskBoardRow: React.FC<TaskBoardRowProps> = ({ taskBoardDto, workspaceUsername, teamUsername }) => {
  return (
    <Button
      className={styles.container}
      variant={ButtonVariants.hoverFilled}
      href={`/${workspaceUsername}/${teamUsername}/task-boards/${taskBoardDto.taskBoardId}`}
    >
      <div className={styles.iconContainer}>
        <IoReaderOutline />
      </div>
      <div className={cn(styles.title, "line-clamp")}>{taskBoardDto.title}</div>
    </Button>
  );
};

export default TaskBoardRow;
