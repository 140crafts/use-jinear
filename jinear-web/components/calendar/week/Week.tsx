import Logger from "@/utils/logger";
import React from "react";
import { ICalendarWeekRowCell } from "../calendarUtils";
import styles from "./Week.module.css";
import Tile from "./tile/Tile";
import WeekDayNumbers from "./weekDayNumbers/WeekDayNumbers";
import WeekRow from "./weekRow/WeekRow";

interface WeekProps {
  weekTasks: ICalendarWeekRowCell[][];
  weekIndex: number;
  week: Date[];
  id: string;
}

const logger = Logger("Week");

const Week: React.FC<WeekProps> = ({ id, week, weekIndex, weekTasks }) => {
  return (
    <div id={id} className={styles.container}>
      <WeekDayNumbers week={week} />
      {weekTasks.map((rowTasks, rowIndex) => (
        <WeekRow
          id={`${id}-row-${rowIndex}`}
          key={`${id}-row-${rowIndex}`}
          rowTasks={rowTasks}
          weekStart={week[0]}
          weekEnd={week[week.length - 1]}
        />
      ))}
      <Tile id={`${id}-tile-container`} week={week} />
    </div>
  );
};

export default Week;
