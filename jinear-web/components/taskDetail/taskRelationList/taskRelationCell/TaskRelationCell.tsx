import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { TaskRelationDto } from "@/model/be/jinear-core";
import React from "react";
import styles from "./TaskRelationCell.module.css";

interface TaskRelationCellProps {
  relation: TaskRelationDto;
}

const TaskRelationCell: React.FC<TaskRelationCellProps> = ({ relation }) => {
  const relatedTask = relation.relatedTask;
  const tag = `${relatedTask.team?.tag}-${relatedTask.teamTagNo}`;
  return (
    <Button
      className={styles.container}
      variant={ButtonVariants.filled}
      heightVariant={ButtonHeight.short}
      href={`/${relatedTask.workspace?.username}/task/${tag}`}
    >
      <div>{tag}</div>
      <div className="single-line flex-1">{relatedTask.title}</div>
    </Button>
  );
};

export default TaskRelationCell;
