import Logger from "@/utils/logger";
import { eachDayOfInterval, endOfMonth, endOfWeek, format, parse, startOfWeek } from "date-fns";
import React, { useState } from "react";
import styles from "./MiniMonthCalendar.module.css";
import DayButton from "./dayButton/DayButton";
import MiniMonthCalendarHeader from "./miniMonthCalendarHeader/MiniMonthCalendarHeader";
import WeekDays from "./weekDays/WeekDays";

interface MiniMonthCalendarProps {
  dayButtonClassName?: string;
  headerContainerClassName?: string;
  value?: Date;
  setValue?: (value?: Date) => void;
  dateSpanStart?: Date;
  dateSpanEnd?: Date;
  disabledBefore?: Date;
  disabledAfter?: Date;
  showYearPicker?: boolean;
  showLabel?: boolean;
}

const logger = Logger("MiniMonthCalendar");

const MiniMonthCalendar: React.FC<MiniMonthCalendarProps> = ({
  dayButtonClassName,
  headerContainerClassName,
  value = new Date(),
  setValue,
  dateSpanStart,
  dateSpanEnd,
  disabledBefore,
  disabledAfter,
  showYearPicker,
  showLabel,
}) => {
  const [viewingDate, setViewingDate] = useState(value);
  const [hoveringDate, setHoveringDate] = useState<Date | undefined>(value);
  const currentMonth = format(viewingDate, "MMM-yyyy");
  const firstDayCurrentMonth = parse(currentMonth, "MMM-yyyy", new Date());
  const periodStart = startOfWeek(firstDayCurrentMonth, { weekStartsOn: 1 });
  const periodEnd = endOfWeek(endOfMonth(firstDayCurrentMonth), { weekStartsOn: 1 });
  const days = eachDayOfInterval({ start: periodStart, end: periodEnd });
  const spanSelection = dateSpanStart || dateSpanEnd;

  const _onMouseLeave = () => {
    setHoveringDate(undefined);
  };

  return (
    <div className={styles.container} onMouseLeave={_onMouseLeave}>
      <MiniMonthCalendarHeader
        containerClassName={headerContainerClassName}
        viewingDate={viewingDate}
        setViewingDate={setViewingDate}
        showYearPicker={showYearPicker}
        showLabel={showLabel}
      />
      <WeekDays days={days} />
      <div className={styles.days}>
        {days.map((day) => (
          <DayButton
            key={`mini-calendar-day-${day.getTime()}`}
            className={dayButtonClassName}
            selectedDate={value}
            viewingDate={viewingDate}
            day={day}
            setSelectedDate={setValue}
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
