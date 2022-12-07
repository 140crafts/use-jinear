import WeeklyTaskView from "@/components/weeklyTaskView/WeeklyTaskView";
import { endOfWeek } from "date-fns";
import React from "react";
import styles from "./TeamWeekView.module.css";

interface TeamWeekViewProps {
  workspaceId: string;
  teamId: string;
  viewingWeekStart: Date;
}

const TeamWeekView: React.FC<TeamWeekViewProps> = ({
  teamId,
  workspaceId,
  viewingWeekStart,
}) => {
  const viewingWeekEnd = endOfWeek(viewingWeekStart, { weekStartsOn: 1 });
  return (
    <div className={styles.container}>
      <WeeklyTaskView
        teamId={teamId}
        workspaceId={workspaceId}
        viewingWeekStart={viewingWeekStart}
        viewingWeekEnd={viewingWeekEnd}
      />
    </div>
  );
};

export default TeamWeekView;
