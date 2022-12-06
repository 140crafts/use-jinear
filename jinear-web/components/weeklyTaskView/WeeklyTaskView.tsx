import { eachDayOfInterval, endOfWeek } from "date-fns";
import React from "react";
import Weekday from "./weekday/Weekday";
import styles from "./WeeklyTaskView.module.css";

interface WeeklyTaskViewProps {
  viewingWeekStart: Date;
}

const WeeklyTaskView: React.FC<WeeklyTaskViewProps> = ({
  viewingWeekStart,
}) => {
  const viewingWeekEnd = endOfWeek(viewingWeekStart, { weekStartsOn: 1 });
  let days = eachDayOfInterval({
    start: viewingWeekStart,
    end: viewingWeekEnd,
  });

  return (
    <div className={styles.container}>
      {days.map((day) => (
        <Weekday key={day.toISOString()} day={day} />
      ))}
    </div>
  );
};

export default WeeklyTaskView;
