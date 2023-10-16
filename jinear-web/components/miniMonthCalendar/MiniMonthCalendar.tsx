import { useFirstRender } from "@/hooks/useFirstRender";
import Logger from "@/utils/logger";
import { eachDayOfInterval, endOfMonth, endOfWeek, format, parse, startOfWeek } from "date-fns";
import React, { useEffect, useState } from "react";
import styles from "./MiniMonthCalendar.module.css";
import DayButton from "./dayButton/DayButton";
import MiniMonthCalendarHeader from "./miniMonthCalendarHeader/MiniMonthCalendarHeader";
import WeekDays from "./weekDays/WeekDays";

interface MiniMonthCalendarProps {
  initialDate?: Date;
  dateSpanStart?: Date;
  dateSpanEnd?: Date;
  disabledBefore?: Date;
  disabledAfter?: Date;
  onDateChange?: (day: Date) => void;
}

const logger = Logger("MiniMonthCalendar");

const MiniMonthCalendar: React.FC<MiniMonthCalendarProps> = ({
  initialDate = new Date(),
  dateSpanStart,
  dateSpanEnd,
  disabledBefore,
  disabledAfter,
  onDateChange,
}) => {
  const firstRender = useFirstRender();
  const [selectedDate, setSelectedDate] = useState(initialDate);
  const [viewingDate, setViewingDate] = useState(initialDate);
  const [hoveringDate, setHoveringDate] = useState(initialDate);
  const currentMonth = format(viewingDate, "MMM-yyyy");
  const firstDayCurrentMonth = parse(currentMonth, "MMM-yyyy", new Date());
  const periodStart = startOfWeek(firstDayCurrentMonth, { weekStartsOn: 1 });
  const periodEnd = endOfWeek(endOfMonth(firstDayCurrentMonth), { weekStartsOn: 1 });
  const days = eachDayOfInterval({ start: periodStart, end: periodEnd });
  const spanSelection = dateSpanStart || dateSpanEnd;

  useEffect(() => {
    if (!firstRender) {
      setViewingDate(selectedDate);
      onDateChange?.(selectedDate);
    }
  }, [selectedDate]);

  logger.log({
    spanSelection,
    dateSpanStart,
    dateSpanEnd,
    selectedDate,
    viewingDate,
    hoveringDate,
    currentMonth,
    firstDayCurrentMonth,
    periodStart,
    periodEnd,
  });
  return (
    <div className={styles.container}>
      <MiniMonthCalendarHeader viewingDate={viewingDate} setViewingDate={setViewingDate} />
      <WeekDays days={days} />
      <div className={styles.days}>
        {days.map((day) => (
          <DayButton
            key={`mini-calendar-day-${day.getTime()}`}
            selectedDate={selectedDate}
            viewingDate={viewingDate}
            day={day}
            setSelectedDate={setSelectedDate}
            dateSpanStart={dateSpanStart}
            dateSpanEnd={dateSpanEnd}
            disabledBefore={disabledBefore}
            disabledAfter={disabledAfter}
            hoveringDate={hoveringDate}
            setHoveringDate={setHoveringDate}
          />
        ))}
      </div>
    </div>
  );
};

export default MiniMonthCalendar;
