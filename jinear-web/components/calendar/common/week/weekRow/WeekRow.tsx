import React from "react";
import { ICalendarWeekRowCell } from "../../../calendarUtils";
import Cell from "../../cell/Cell";
import styles from "./WeekRow.module.css";

interface WeekRowProps {
  rowTasks: ICalendarWeekRowCell[];
  id: string;
  weekStart: Date;
  weekEnd: Date;
}

const WeekRow: React.FC<WeekRowProps> = ({ id, rowTasks, weekStart, weekEnd }) => {
  return (
    <div className={styles.container}>
      {rowTasks?.map((cell, cellIndex) => (
        <Cell id={`${id}-cell-${cellIndex}`} key={`${id}-cell-${cellIndex}`} weekStart={weekStart} weekEnd={weekEnd} {...cell} />
      ))}
    </div>
  );
};

export default WeekRow;
