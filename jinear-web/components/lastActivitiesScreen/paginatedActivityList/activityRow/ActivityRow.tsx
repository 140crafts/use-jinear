import Button from "@/components/button";
import TaskActivity from "@/components/taskDetail/taskActivityList/taskActivity/TaskActivity";
import { WorkspaceActivityDto } from "@/model/be/jinear-core";
import React from "react";
import styles from "./ActivityRow.module.css";

interface ActivityRowProps {
  activity: WorkspaceActivityDto;
  sameGroup: boolean | null;
  ignoreGrouping?: boolean;
}

const ActivityRow: React.FC<ActivityRowProps> = ({ sameGroup, ignoreGrouping, activity }) => {
  return (
    <div className={styles.container}>
      {!sameGroup && !ignoreGrouping && activity.groupTitle && (
        <Button className={styles.titleContainer} href={activity.groupLink || undefined}>
          <b className={styles.groupTitle}>{activity.groupTitle}</b>
        </Button>
      )}
      <TaskActivity activity={activity} />
    </div>
  );
};

export default ActivityRow;
