import Button from "@/components/button";
import { format } from "date-fns";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoCaretBack, IoCaretForward, IoEllipse } from "react-icons/io5";
import { useViewingDate } from "../context/CalendarContext";
import styles from "./CalendarHeader.module.css";

interface CalendarHeaderProps {
  days: Date[];
}

const CalendarHeader: React.FC<CalendarHeaderProps> = ({ days }) => {
  const { t } = useTranslation();
  const viewingDate = useViewingDate();
  const title = !viewingDate
    ? ""
    : format(viewingDate, "MMMM yy", {
        locale: t("dateFnsLocale") as any,
      });

  const prevMonth = () => {};
  const thisMonth = () => {};
  const nextMonth = () => {};

  return (
    <>
      <div className={styles.mainCalendarHeader}>
        <h1 className={styles.monthHeader}>{title}</h1>
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
      <div className={styles.weekdayHeaderContainer}>
        {days.slice(0, 7).map((day) => (
          <div key={`header-${day.getTime()}`} className={styles.weekdayHeader}>
            {format(day, "E", { weekStartsOn: 1 })}
          </div>
        ))}
      </div>
    </>
  );
};

export default CalendarHeader;
