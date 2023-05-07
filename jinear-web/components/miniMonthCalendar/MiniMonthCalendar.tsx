import { useFirstRender } from "@/hooks/useFirstRender";
import Logger from "@/utils/logger";
import cn from "classnames";
import { eachDayOfInterval, endOfMonth, endOfWeek, format, isSameDay, isSameMonth, isToday, parse, startOfWeek } from "date-fns";
import React, { useEffect, useState } from "react";
import Button, { ButtonVariants } from "../button";
import styles from "./MiniMonthCalendar.module.css";
import MiniMonthCalendarHeader from "./miniMonthCalendarHeader/MiniMonthCalendarHeader";
import WeekDays from "./weekDays/WeekDays";

interface MiniMonthCalendarProps {
  initialDate?: Date;
  onDateChange?: (day: Date) => void;
}

const logger = Logger("MiniMonthCalendar");

const MiniMonthCalendar: React.FC<MiniMonthCalendarProps> = ({ initialDate = new Date(), onDateChange }) => {
  const firstRender = useFirstRender();
  const [selectedDate, setSelectedDate] = useState(initialDate);
  const [viewingDate, setViewingDate] = useState(initialDate);
  const currentMonth = format(viewingDate, "MMM-yyyy");
  const firstDayCurrentMonth = parse(currentMonth, "MMM-yyyy", new Date());
  const periodStart = startOfWeek(firstDayCurrentMonth, { weekStartsOn: 1 });
  const periodEnd = endOfWeek(endOfMonth(firstDayCurrentMonth), { weekStartsOn: 1 });
  const days = eachDayOfInterval({ start: periodStart, end: periodEnd });

  useEffect(() => {
    if (!firstRender) {
      setViewingDate(selectedDate);
      onDateChange?.(selectedDate);
    }
  }, [selectedDate]);

  return (
    <div className={styles.container}>
      <MiniMonthCalendarHeader viewingDate={viewingDate} setViewingDate={setViewingDate} />
      <WeekDays days={days} />
      <div className={styles.days}>
        {days.map((day) => (
          <Button
            key={`mini-calendar-day-${day.getTime()}`}
            variant={isSameDay(selectedDate, day) ? ButtonVariants.filled : ButtonVariants.default}
            className={cn(
              styles.buttonBase,
              !isSameMonth(viewingDate, day) ? styles.differentMonth : undefined,
              isSameDay(selectedDate, day) ? styles.bold : undefined
            )}
            onClick={() => setSelectedDate(day)}
          >
            <div className={cn(isToday(day) ? styles.today : undefined)}>{format(day, "d")}</div>
          </Button>
        ))}
      </div>
    </div>
  );
};

export default MiniMonthCalendar;
