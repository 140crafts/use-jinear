import { WorkspaceActivityDto } from "@/model/be/jinear-core";
import React from "react";
import styles from "./WorkflowStatusDiffInfo.module.css";

interface WorkflowStatusDiffInfoProps {
  activity: WorkspaceActivityDto;
}

const WorkflowStatusDiffInfo: React.FC<WorkflowStatusDiffInfoProps> = ({ activity }) => {
  const oldWorkflowStatusName = activity.oldWorkflowStatusDto?.name || "";
  const newWorkflowStatusName = activity.newWorkflowStatusDto?.name || "";

  return (
    <div className={styles.container}>
      <div className={styles.cell}>{oldWorkflowStatusName}</div>
      <div className={styles.arrow}>{"->"}</div>
      <div className={styles.cell}>{newWorkflowStatusName}</div>
    </div>
  );
};

export default WorkflowStatusDiffInfo;
