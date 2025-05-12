import { WorkspaceActivityDto } from "@/model/be/jinear-core";
import React from "react";
import styles from "./DescDiffInfo.module.css";

interface DescDiffInfoProps {
  activity: WorkspaceActivityDto;
}

const DescDiffInfo: React.FC<DescDiffInfoProps> = ({ activity }) => {
  const oldDesc = activity.oldDescription?.value || "";
  const newDesc = activity.newDescription?.value || "";

  return (
    <div className={styles.contentContainer}>
      <div className={styles.cell} dangerouslySetInnerHTML={{ __html: oldDesc }} />
      <div className={styles.arrow}>{"->"}</div>
      <div className={styles.cell} dangerouslySetInnerHTML={{ __html: newDesc }} />
    </div>
  );
};

export default DescDiffInfo;
