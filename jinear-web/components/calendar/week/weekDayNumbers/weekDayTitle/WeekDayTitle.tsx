import { useViewingDate } from "@/components/calendar/context/CalendarContext";
import cn from "classnames";
import { format, isSameMonth } from "date-fns";
import React from "react";
import styles from "./WeekDayTitle.module.css";

interface WeekDayTitleProps {
  day: Date;
}

const WeekDayTitle: React.FC<WeekDayTitleProps> = ({ day }) => {
  const viewingDate = useViewingDate();
  const isCurrentDayAndVieweingDateInDifferentMonth = !isSameMonth(viewingDate, day);

  return (
    <div className={cn(styles.titleContainer, isCurrentDayAndVieweingDateInDifferentMonth && styles.differentMonthTitle)}>
      {format(day, "dd")}
    </div>
  );
};

export default WeekDayTitle;
