import { WorkspaceActivityDto } from "@/model/be/jinear-core";
import { format } from "date-fns";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./AssignedDateChangeDiffInfo.module.css";

interface AssignedDateChangeDiffInfoProps {
  activity: WorkspaceActivityDto;
}

const AssignedDateChangeDiffInfo: React.FC<AssignedDateChangeDiffInfoProps> = ({ activity }) => {
  const { t } = useTranslation();
  const oldVal = activity.oldState
    ? format(new Date(activity.oldState), t("dateFormat"))
    : t("taskWorkflowActivityInfoAssigneeNoDate");
  const newVal = activity.newState
    ? format(new Date(activity.newState), t("dateFormat"))
    : t("taskWorkflowActivityInfoAssigneeNoDate");

  return (
    <div className={styles.container}>
      <div className={styles.cell}>{oldVal}</div>
      <div className={styles.arrow}>{"->"}</div>
      <div className={styles.cell}>{newVal}</div>
    </div>
  );
};

export default AssignedDateChangeDiffInfo;
