import Line from "@/components/line/Line";
import React from "react";
import WeekdayTitle from "./title/WeekdayTitle";
import styles from "./Weekday.module.css";

interface WeekdayProps {
  day: Date;
  taskCount?: number;
}

const Weekday: React.FC<WeekdayProps> = ({ day, taskCount = 0 }) => {
  return (
    <div className={styles.container}>
      <WeekdayTitle day={day} taskCount={taskCount} />
      <Line />
    </div>
  );
};

export default Weekday;
