import { WorkspaceActivityDto } from "@/model/be/jinear-core";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./AssigneeChangeDiffInfo.module.css";

interface AssigneeChangeDiffInfoProps {
  activity: WorkspaceActivityDto;
}

const AssigneeChangeDiffInfo: React.FC<AssigneeChangeDiffInfoProps> = ({
  activity,
}) => {
  const { t } = useTranslation();
  const oldUsername =
    activity.oldAssignedToAccount?.username ||
    t("taskWorkflowActivityInfoAssigneeNoone");
  const newUsername =
    activity.newAssignedToAccount?.username ||
    t("taskWorkflowActivityInfoAssigneeNoone");

  return (
    <div className={styles.container}>
      <div className={styles.cell}>{oldUsername}</div>
      <div className={styles.arrow}>{"->"}</div>
      <div className={styles.cell}>{newUsername}</div>
    </div>
  );
};

export default AssigneeChangeDiffInfo;
