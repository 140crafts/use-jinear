import Logger from "@/utils/logger";
import { eachDayOfInterval, endOfMonth, endOfWeek, format, isSameDay, isSameMonth, parse, startOfWeek } from "date-fns";
import React, { useEffect, useState } from "react";
import Button, { ButtonVariants } from "../button";
import styles from "./MiniMonthCalendar.module.css";
import MiniMonthCalendarHeader from "./miniMonthCalendarHeader/MiniMonthCalendarHeader";
import WeekDays from "./weekDays/WeekDays";

interface MiniMonthCalendarProps {
  selectedDate?: Date;
  onDateChange?: (day: Date) => void;
}

const logger = Logger("MiniMonthCalendar");

const MiniMonthCalendar: React.FC<MiniMonthCalendarProps> = ({ selectedDate = new Date(), onDateChange }) => {
  const [viewingDate, setViewingDate] = useState(selectedDate);
  const currentMonth = format(viewingDate, "MMM-yyyy");
  const firstDayCurrentMonth = parse(currentMonth, "MMM-yyyy", new Date());
  const periodStart = startOfWeek(firstDayCurrentMonth, { weekStartsOn: 1 });
  const periodEnd = endOfWeek(endOfMonth(firstDayCurrentMonth), { weekStartsOn: 1 });
  const days = eachDayOfInterval({ start: periodStart, end: periodEnd });
  useEffect(() => {
    setViewingDate(selectedDate);
  }, [selectedDate]);
  useEffect(() => {
    onDateChange?.(viewingDate);
  }, [viewingDate]);

  return (
    <div className={styles.container}>
      <MiniMonthCalendarHeader viewingDate={viewingDate} setViewingDate={setViewingDate} />
      <WeekDays days={days} />
      <div className={styles.days}>
        {days.map((day) => (
          <Button
            key={`mini-calendar-day-${day.getTime()}`}
            variant={isSameDay(viewingDate, day) ? ButtonVariants.filled : ButtonVariants.default}
            className={!isSameMonth(viewingDate, day) ? styles.differentMonth : undefined}
            onClick={() => setViewingDate(day)}
          >
            {format(day, "d")}
          </Button>
        ))}
      </div>
    </div>
  );
};

export default MiniMonthCalendar;
