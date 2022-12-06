import WeeklyTaskView from "@/components/weeklyTaskView/WeeklyTaskView";
import React from "react";
import styles from "./TeamWeekView.module.css";

interface TeamWeekViewProps {
  viewingWeekStart: Date;
}

const TeamWeekView: React.FC<TeamWeekViewProps> = ({ viewingWeekStart }) => {
  return (
    <div className={styles.container}>
      <WeeklyTaskView viewingWeekStart={viewingWeekStart} />
    </div>
  );
};

export default TeamWeekView;
