import React from "react";
import { ICalendarWeekRowCell } from "../calendarUtils";
import Week from "../week/Week";
import styles from "./Month.module.css";

interface MonthProps {
  monthTable: ICalendarWeekRowCell[][][];
  days: Date[];
}

const Month: React.FC<MonthProps> = ({ monthTable, days }) => {
  return (
    <div className={styles.weeksContainer}>
      {monthTable?.map((week, weekIndex) => (
        <Week
          id={`week-${weekIndex}`}
          key={`week-${weekIndex}`}
          weekTasks={week}
          weekIndex={weekIndex}
          week={days.slice(weekIndex * 7, weekIndex * 7 + 7)}
        />
      ))}
    </div>
  );
};
export default Month;
