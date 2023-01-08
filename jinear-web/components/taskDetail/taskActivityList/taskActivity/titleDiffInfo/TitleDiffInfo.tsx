import { WorkspaceActivityDto } from "@/model/be/jinear-core";
import React from "react";
import styles from "./TitleDiffInfo.module.css";

interface TitleDiffInfoProps {
  activity: WorkspaceActivityDto;
}

const MAX_CHAR = 36;

const TitleDiffInfo: React.FC<TitleDiffInfoProps> = ({ activity }) => {
  const oldTitleTooLong = activity.oldState && activity.oldState.length > 36;
  const oldTitle =
    activity.oldState?.substring(0, MAX_CHAR) + (oldTitleTooLong ? "..." : "");

  const newTitleTooLong = activity.newState && activity.newState.length > 36;
  const newTitle =
    activity.newState?.substring(0, MAX_CHAR) + (newTitleTooLong ? "..." : "");

  return (
    <div className={styles.container}>
      <div
        className={styles.cell}
        data-tooltip-multiline={oldTitleTooLong ? activity.oldState : undefined}
      >
        {oldTitle}
      </div>
      {"->"}
      <div
        className={styles.cell}
        data-tooltip-multiline={newTitleTooLong ? activity.newState : undefined}
      >
        {newTitle}
      </div>
    </div>
  );
};

export default TitleDiffInfo;
