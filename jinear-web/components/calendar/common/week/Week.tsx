import Logger from "@/utils/logger";
import cn from "classnames";
import React from "react";
import { ICalendarWeekRowCell } from "../../calendarUtils";
import styles from "./Week.module.css";
import WeekDayNumbers from "./weekDayNumbers/WeekDayNumbers";
import WeekRow from "./weekRow/WeekRow";
import WeekTileRow from "./weekTileRow/WeekTileRow";

interface WeekProps {
  weekTasks: ICalendarWeekRowCell[][];
  weekIndex: number;
  week: Date[];
  id: string;
  omitNumbers?: boolean;
  className?: string;
}

const logger = Logger("Week");

const Week: React.FC<WeekProps> = ({ id, week, weekIndex, weekTasks, omitNumbers = false, className }) => {
  return (
    <div id={id} className={cn(styles.container, className)}>
      {!omitNumbers && <WeekDayNumbers week={week} />}
      <div className={styles.weekRowContainer}>
        {weekTasks?.map((rowTasks, rowIndex) => (
          <WeekRow
            id={`${id}-row-${rowIndex}`}
            key={`${id}-row-${rowIndex}`}
            rowTasks={rowTasks}
            weekStart={week[0]}
            weekEnd={week[week.length - 1]}
          />
        ))}
      </div>
      <WeekTileRow id={`${id}-tile-container`} week={week} />
    </div>
  );
};

export default Week;
