import { WorkspaceActivityDto } from "@/model/be/jinear-core";
import React from "react";
import styles from "./TaskRelationChangedDiffInfo.module.css";

interface TaskRelationChangedDiffInfoProps {
  activity: WorkspaceActivityDto;
}

const TaskRelationChangedDiffInfo: React.FC<
  TaskRelationChangedDiffInfoProps
> = ({ activity }) => {
  const field =
    activity.type == "RELATION_INITIALIZED"
      ? "newTaskRelationDto"
      : "oldTaskRelationDto";
  const relation = activity[field];
  const taskTag = relation?.task.team?.tag
    ? relation?.task.team?.tag + relation?.task.teamTagNo
    : undefined;
  const relatedTaskTag = relation?.relatedTask.team?.tag
    ? relation?.relatedTask.team?.tag + relation?.relatedTask.teamTagNo
    : undefined;

  return (
    <div className={styles.container}>
      <div className={styles.cell}>{taskTag}</div>
      <div className={styles.arrow}>{"<->"}</div>
      <div className={styles.cell}>{relatedTaskTag}</div>
    </div>
  );
};

export default TaskRelationChangedDiffInfo;
