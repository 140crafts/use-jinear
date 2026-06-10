import Button, { ButtonVariants } from "@/components/button";
import Logger from "@/utils/logger";
import cn from "classnames";
import { format, isAfter, isBefore, isSameDay, isSameMonth, isToday } from "date-fns";
import React from "react";
import styles from "./DayButton.module.css";

interface DayButtonProps {
  className?: string;
  selectedDate: Date;
  viewingDate: Date;
  day: Date;
  setSelectedDate?: (value?: Date) => void;
  dateSpanStart?: Date;
  dateSpanEnd?: Date;
  disabledBefore?: Date;
  disabledAfter?: Date;
  hoveringDate?: Date;
  setHoveringDate?: React.Dispatch<React.SetStateAction<Date | undefined>>;
}

const logger = Logger("DayButton");
const DayButton: React.FC<DayButtonProps> = ({
  day,
  className,
  selectedDate,
  viewingDate,
  setSelectedDate,
  dateSpanStart,
  dateSpanEnd,
  disabledBefore,
  disabledAfter,
  hoveringDate,
  setHoveringDate,
}) => {
  const matchesDisabledBefore = disabledBefore ? isBefore(day, disabledBefore) && !isSameDay(day, disabledBefore) : false;
  const matchesDisabledAfter = disabledAfter ? isAfter(day, disabledAfter) && !isSameDay(day, disabledAfter) : false;
  const isDisabled = matchesDisabledBefore || matchesDisabledAfter;

  const spanEndDate = dateSpanEnd ? dateSpanEnd : hoveringDate;
  const isCurrentDaySpanStart = dateSpanStart ? isSameDay(dateSpanStart, day) : false;
  const isCurrentDaySpanEnd = spanEndDate ? isSameDay(spanEndDate, day) : false;

  const isSpanEndDateAfterSpanStart = spanEndDate && dateSpanStart ? spanEndDate.getTime() > dateSpanStart.getTime() : false;
  const isSpanEndDateBeforeSpanStart = spanEndDate && dateSpanStart ? spanEndDate.getTime() < dateSpanStart.getTime() : false;

  const isDateBetweenHoveringDate =
    spanEndDate && dateSpanStart
      ? Math.min(spanEndDate.getTime(), dateSpanStart.getTime()) < day.getTime() &&
        day.getTime() < Math.max(spanEndDate.getTime(), dateSpanStart.getTime())
      : false;

  const onHover = () => {
    setHoveringDate?.(day);
  };

  return (
    <Button
      key={`mini-calendar-day-${day.getTime()}`}
      disabled={isDisabled}
      variant={isSameDay(selectedDate, day) ? ButtonVariants.filled : ButtonVariants.default}
      className={cn(
        styles.buttonBase,
        className,
        !isSameMonth(viewingDate, day) ? styles.differentMonth : undefined,
        isSameDay(selectedDate, day) ? styles.bold : undefined,
        isToday(day) ? styles.today : undefined,
        isCurrentDaySpanStart
          ? isSpanEndDateAfterSpanStart
            ? styles.borderRightRadius
            : isSpanEndDateBeforeSpanStart
            ? styles.borderLeftRadius
            : undefined
          : undefined,
        isDateBetweenHoveringDate ? styles.betweenHoveringDay : undefined,
        isCurrentDaySpanEnd ? styles.spanEnd : undefined,
        isCurrentDaySpanEnd
          ? isSpanEndDateAfterSpanStart
            ? styles.borderLeftRadius
            : isSpanEndDateBeforeSpanStart
            ? styles.borderRightRadius
            : undefined
          : undefined,
        isDisabled ? styles.disabled : undefined
      )}
      onClick={() => setSelectedDate?.(day)}
      onMouseOver={onHover}
    >
      <div>{format(day, "d")}</div>
    </Button>
  );
};

export default DayButton;
