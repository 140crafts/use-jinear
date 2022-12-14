import React from "react";
import WeekdayTitle from "./title/WeekdayTitle";
import styles from "./Weekday.module.css";

interface WeekdayProps {
  day: Date;
  taskCount?: number;
  showDayOfWeek?: boolean;
}

const Weekday: React.FC<WeekdayProps> = ({
  day,
  taskCount = 0,
  showDayOfWeek,
}) => {
  return (
    <div className={styles.container}>
      <WeekdayTitle
        day={day}
        taskCount={taskCount}
        showDayOfWeek={showDayOfWeek}
      />
      {/* <Line /> */}
    </div>
  );
};

export default Weekday;
