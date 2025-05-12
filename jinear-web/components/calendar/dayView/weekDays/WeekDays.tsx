import Button from "@/components/button";
import {
  queryStateDateToShortDateConverter,
  queryStateShortDateParser,
  useQueryState,
  useSetQueryState,
} from "@/hooks/useQueryState";
import cn from "classnames";
import { format, isSameDay, startOfDay, startOfToday } from "date-fns";
import React from "react";
import styles from "./WeekDays.module.css";

interface WeekDaysProps {
  days: Date[];
}

const WeekDays: React.FC<WeekDaysProps> = ({ days }) => {
  const setQueryState = useSetQueryState();
  const viewingDate = useQueryState<Date>("viewingDate", queryStateShortDateParser) || startOfDay(new Date());

  const setViewingDate = (viewingDate: Date) => {
    setQueryState("viewingDate", queryStateDateToShortDateConverter(viewingDate));
  };

  return (
    <div className={styles.weekViewWeekdayHeaderContainer}>
      {days.slice(0, 7).map((day) => {
        const isToday = isSameDay(startOfToday(), day);
        const isViewingDate = isSameDay(viewingDate, day);
        return (
          <Button
            key={`header-${day.getTime()}`}
            id={isToday ? "calendar-weekday-title-today" : undefined}
            className={cn(styles.weekdayHeader, isToday && styles.today, isViewingDate && styles.viewingDate)}
            onClick={() => setViewingDate?.(day)}
          >
            <div className={styles.dayOfWeek}>{format(day, "E", { weekStartsOn: 1 })}</div>
            <div className={styles.dayNumber}>{format(day, "dd", { weekStartsOn: 1 })}</div>
          </Button>
        );
      })}
    </div>
  );
};

export default WeekDays;
