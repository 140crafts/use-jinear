import Button, { ButtonVariants } from "@/components/button";
import { TaskListDto } from "@/model/be/jinear-core";
import cn from "classnames";
import React from "react";
import { IoReaderOutline } from "react-icons/io5";
import styles from "./TaskListRow.module.css";

interface TaskListRowProps {
  taskListDto: TaskListDto;
}

const TaskListRow: React.FC<TaskListRowProps> = ({ taskListDto }) => {
  return (
    <Button className={styles.container} variant={ButtonVariants.hoverFilled}>
      <div className={styles.iconContainer}>
        <IoReaderOutline />
      </div>
      <div className={cn(styles.title, "line-clamp")}>{taskListDto.title}</div>
    </Button>
  );
};

export default TaskListRow;
