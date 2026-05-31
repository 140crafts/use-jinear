import cn from "classnames";
import { format, isSameDay, startOfToday } from "date-fns";
import React from "react";
import styles from "./WeekDays.module.css";

interface WeekDaysProps {
  days: Date[];
}

const WeekDays: React.FC<WeekDaysProps> = ({ days }) => {
  return (
    <div className={styles.weekViewWeekdayHeaderContainer}>
      <div className={styles.sideHourLabelSpacer} />
      {days.slice(0, Math.min(days.length, 7)).map((day) => {
        const isToday = isSameDay(startOfToday(), day);
        return (
          <div
            key={`header-${day.getTime()}`}
            id={isToday ? "calendar-weekday-title-today" : undefined}
            className={cn(styles.weekdayHeader, isToday && styles.today)}
          >
            <div>{format(day, "dd", { weekStartsOn: 1 })}</div>
            <div>{format(day, "E", { weekStartsOn: 1 })}</div>
          </div>
        );
      })}
    </div>
  );
};

export default WeekDays;
