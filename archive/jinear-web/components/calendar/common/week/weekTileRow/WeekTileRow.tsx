import React from "react";
import styles from "./WeekTileRow.module.css";

interface WeekTileRowProps {
  id: string;
  week: Date[];
}

import Logger from "@/utils/logger";
import WeekTile from "./weekTile/WeekTile";

const logger = Logger("WeekTile");

const WeekTileRow: React.FC<WeekTileRowProps> = ({ id, week }) => {
  return (
    <div className={styles.calendarLineContainer}>
      {Array.from(Array(week.length).keys()).map((i) => (
        <WeekTile key={`${id}-tile-${i}`} day={week[i]} />
      ))}
    </div>
  );
};

export default WeekTileRow;
