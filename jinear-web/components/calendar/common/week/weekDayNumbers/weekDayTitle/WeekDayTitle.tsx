import { useViewingDate } from "@/components/calendar/context/CalendarContext";
import cn from "classnames";
import { format, isSameDay, isSameMonth, startOfToday } from "date-fns";
import React, { useRef } from "react";
import styles from "./WeekDayTitle.module.css";

interface WeekDayTitleProps {
  day: Date;
}

const WeekDayTitle: React.FC<WeekDayTitleProps> = ({ day }) => {
  const containerRef = useRef<HTMLDivElement>(null);
  const viewingDate = useViewingDate();
  const isCurrentDayAndVieweingDateInDifferentMonth = !isSameMonth(viewingDate, day);
  const isToday = isSameDay(startOfToday(), day);

  return (
    <div
      id={isToday ? "calendar-title-today" : undefined}
      ref={containerRef}
      className={cn(
        styles.titleContainer,
        isCurrentDayAndVieweingDateInDifferentMonth && styles.differentMonthContainer,
        isToday && styles.today
      )}
    >
      {format(day, "dd")}
    </div>
  );
};

export default WeekDayTitle;
