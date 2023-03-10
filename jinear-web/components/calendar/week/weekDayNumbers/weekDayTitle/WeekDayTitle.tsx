import { useViewingDate } from "@/components/calendar/context/CalendarContext";
import cn from "classnames";
import { format, isSameDay, isSameMonth, startOfToday } from "date-fns";
import React from "react";
import styles from "./WeekDayTitle.module.css";

interface WeekDayTitleProps {
  day: Date;
}

const WeekDayTitle: React.FC<WeekDayTitleProps> = ({ day }) => {
  const viewingDate = useViewingDate();
  const isCurrentDayAndVieweingDateInDifferentMonth = !isSameMonth(viewingDate, day);
  const isToday = isSameDay(startOfToday(), day);

  return (
    <div
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
