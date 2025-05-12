import Button from "@/components/button";
import cn from "classnames";
import { addMonths, format, setYear, startOfToday } from "date-fns";
import React, { ChangeEvent, Dispatch, SetStateAction } from "react";
import { IoCaretBack, IoCaretForward, IoEllipse } from "react-icons/io5";
import styles from "./MiniMonthCalendarHeader.module.css";

interface MiniMonthCalendarHeaderProps {
  containerClassName?: string;
  viewingDate: Date;
  setViewingDate: Dispatch<SetStateAction<Date>>;
  showYearPicker?: boolean;
  showLabel?: boolean;
}
const START_YEAR = 1900;
const YEAR_RANGE = 201;

const MiniMonthCalendarHeader: React.FC<MiniMonthCalendarHeaderProps> = ({
  containerClassName,
  viewingDate,
  setViewingDate,
  showYearPicker = true,
  showLabel = true,
}) => {
  const viewingDateYear = format(viewingDate, "yyyy");

  const onYearSelectChange = (e: ChangeEvent<HTMLSelectElement>) => {
    const newYear = parseInt(e.target.value);
    const newDate = setYear(viewingDate, newYear);
    setViewingDate(newDate);
  };

  const prevMonth = () => {
    if (viewingDate) {
      setViewingDate?.(addMonths(viewingDate, -1));
    }
  };

  const nextMonth = () => {
    if (viewingDate) {
      setViewingDate?.(addMonths(viewingDate, 1));
    }
  };

  const thisMonth = () => {
    setViewingDate?.(startOfToday());
  };

  return (
    <div className={cn(styles.container, containerClassName)}>
      <div className={styles.infoContainer}>
        {showYearPicker && (
          <div>
            <select onChange={onYearSelectChange} value={viewingDateYear} className={styles.dateLabel}>
              {[...new Array(YEAR_RANGE)].map((_, index) => (
                <option key={`mini-month-calendar-header-year-picker-${index}`} value={index + START_YEAR}>
                  {index + START_YEAR}
                </option>
              ))}
            </select>
          </div>
        )}
        {showLabel && <div className={cn(styles.dateLabel, "single-line")}>{format(viewingDate, "LLLL")}</div>}
      </div>
      {/* <div className="flex-1" /> */}
      <div className={styles.calendarNavigation}>
        <Button onClick={prevMonth} className={styles.iconButton}>
          <IoCaretBack />
        </Button>
        <Button onClick={thisMonth} className={styles.iconButton}>
          <IoEllipse size={7} />
        </Button>
        <Button onClick={nextMonth} className={styles.iconButton}>
          <IoCaretForward />
        </Button>
      </div>
    </div>
  );
};

export default MiniMonthCalendarHeader;
