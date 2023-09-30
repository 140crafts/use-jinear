import { format } from "date-fns";
import React from "react";
import styles from "./WeekDays.module.css";

interface WeekDaysProps {
  days: Date[];
}

const WeekDays: React.FC<WeekDaysProps> = ({ days }) => {
  return (
    <div className={styles.weekViewWeekdayHeaderContainer}>
      <div className={styles.sideHourLabelSpacer} />
      {days.slice(0, 7).map((day) => (
        <div key={`header-${day.getTime()}`} className={styles.weekdayHeader}>
          {format(day, "E", { weekStartsOn: 1 })}
        </div>
      ))}
    </div>
  );
};

export default WeekDays;
