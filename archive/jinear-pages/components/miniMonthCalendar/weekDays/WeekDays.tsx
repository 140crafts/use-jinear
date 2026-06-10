import { format } from "date-fns";
import React from "react";
import styles from "./WeekDays.module.css";

interface WeekDaysProps {
  days: Date[];
}

const WeekDays: React.FC<WeekDaysProps> = ({ days }) => {
  return (
    <div className={styles.container}>
      {days.slice(0, 7).map((day) => (
        <div key={`mini-cal-header-${day.getTime()}`} className={styles.weekdayHeader}>
          {format(day, "EEE", { weekStartsOn: 1 })}
        </div>
      ))}
    </div>
  );
};

export default WeekDays;
