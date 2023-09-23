import { getOffset, getSize } from "@/utils/htmlUtis";
import Logger from "@/utils/logger";
import cn from "classnames";
import { format, isThisMonth } from "date-fns";
import React, { useEffect, useRef } from "react";
import { ICalendarWeekRowCell } from "../calendarUtils";
import { useViewingDate } from "../context/CalendarContext";
import Week from "../week/Week";
import styles from "./Month.module.scss";

interface MonthProps {
  monthTable: ICalendarWeekRowCell[][][];
  days: Date[];
  squeezedView: boolean;
}

const logger = Logger("Month");
const Month: React.FC<MonthProps> = ({ monthTable, days, squeezedView }) => {
  const weeksContainerRef = useRef<HTMLDivElement>(null);
  const viewingDate = useViewingDate();

  useEffect(() => {
    if (viewingDate && isThisMonth(viewingDate)) {
      setTimeout(() => {
        const todayTitle = document.getElementById("calendar-title-today");
        if (todayTitle) {
          const offset = getOffset(todayTitle);
          const size = getSize(todayTitle);

          weeksContainerRef.current?.scrollTo?.({
            left: offset.left + size.width,
            top: offset.top - size.height,
            behavior: "smooth",
          });
          scroll({ left: offset.left + size.width, top: offset.top - size.height, behavior: "smooth" });
        }
      }, 500);
    }
  }, [viewingDate]);

  return (
    <div id="weeks-container" ref={weeksContainerRef} className={styles.weeksContainer}>
      <div className={cn(styles.weeksBaseContentContainer, squeezedView ? undefined : styles.weeksContentContainer)}>
        <div className={styles.weekdayHeaderContainer}>
          {days.slice(0, 7).map((day) => (
            <div key={`header-${day.getTime()}`} className={styles.weekdayHeader}>
              {format(day, "E", { weekStartsOn: 1 })}
            </div>
          ))}
        </div>
        {monthTable?.map((week, weekIndex) => (
          <Week
            id={`week-${weekIndex}`}
            key={`week-${weekIndex}`}
            weekTasks={week}
            weekIndex={weekIndex}
            week={days.slice(weekIndex * 7, weekIndex * 7 + 7)}
          />
        ))}
      </div>
    </div>
  );
};
export default Month;
