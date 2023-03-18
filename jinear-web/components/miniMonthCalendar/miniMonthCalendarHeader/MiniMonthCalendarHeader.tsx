import Button from "@/components/button";
import { addMonths, format, setYear, startOfToday } from "date-fns";
import React, { ChangeEvent, Dispatch, SetStateAction } from "react";
import { IoCaretBack, IoCaretForward, IoEllipse } from "react-icons/io5";
import styles from "./MiniMonthCalendarHeader.module.css";

interface MiniMonthCalendarHeaderProps {
  viewingDate: Date;
  setViewingDate: Dispatch<SetStateAction<Date>>;
}
const START_YEAR = 1900;
const YEAR_RANGE = 201;

const MiniMonthCalendarHeader: React.FC<MiniMonthCalendarHeaderProps> = ({ viewingDate, setViewingDate }) => {
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
    <div className={styles.container}>
      <div>
        <select onChange={onYearSelectChange} value={viewingDateYear} className={styles.dateLabel}>
          {[...new Array(YEAR_RANGE)].map((_, index) => (
            <option key={`mini-month-calendar-header-year-picker-${index}`} value={index + START_YEAR}>
              {index + START_YEAR}
            </option>
          ))}
        </select>
      </div>
      <div className={styles.dateLabel}>{format(viewingDate, "LLLL")}</div>
      <div className="flex-1" />
      <div className={styles.calendarNavigation}>
        <Button onClick={prevMonth}>
          <IoCaretBack />
        </Button>
        <Button onClick={thisMonth}>
          <IoEllipse size={7} />
        </Button>
        <Button onClick={nextMonth}>
          <IoCaretForward />
        </Button>
      </div>
    </div>
  );
};

export default MiniMonthCalendarHeader;
