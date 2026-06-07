import { queryStateShortDateParser, useQueryState } from "@/hooks/useQueryState";
import cn from "classnames";
import { format, isSameDay, isSameMonth, startOfDay, startOfToday } from "date-fns";
import React, { useRef } from "react";
import styles from "./WeekDayTitle.module.css";

interface WeekDayTitleProps {
  day: Date;
}

const WeekDayTitle: React.FC<WeekDayTitleProps> = ({ day }) => {
  const containerRef = useRef<HTMLDivElement>(null);
  const viewingDate = useQueryState<Date>("viewingDate", queryStateShortDateParser) || startOfDay(new Date());
  const isCurrentDayAndVieweingDateInDifferentMonth = !isSameMonth(viewingDate, day);
  const isToday = isSameDay(startOfToday(), day);

  return (
    <div
      id={isToday ? "calendar-title-today" : undefined}
      ref={containerRef}
      className={cn(styles.titleContainer, isCurrentDayAndVieweingDateInDifferentMonth && styles.differentMonthContainer)}
    >
      <div className={cn(styles.dayNo, isToday ? styles.today : undefined)}>{format(day, "dd")}</div>
    </div>
  );
};

export default WeekDayTitle;
