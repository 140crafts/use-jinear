import { queryStateShortDateParser, useQueryState } from "@/hooks/useQueryState";
import { getOffset, getSize } from "@/utils/htmlUtils";
import Logger from "@/utils/logger";
import cn from "classnames";
import { format, isThisMonth, startOfDay } from "date-fns";
import React, { useEffect } from "react";
import { ICalendarWeekRowCell } from "../../calendarUtils";
import Week from "../../common/week/Week";
import styles from "./Month.module.scss";

interface MonthProps {
  monthTable: ICalendarWeekRowCell[][][];
  days: Date[];
  squeezedView: boolean;
}

const logger = Logger("Month");

const Month: React.FC<MonthProps> = ({ monthTable, days, squeezedView }) => {
  const viewingDate = useQueryState<Date>("viewingDate", queryStateShortDateParser) || startOfDay(new Date());

  useEffect(() => {
    if (viewingDate && isThisMonth(viewingDate)) {
      setTimeout(() => {
        const todayTitle = document.getElementById("calendar-title-today");
        const pageContent = document.getElementById("workspace-layout-page-content");
        if (todayTitle && pageContent) {
          const offset = getOffset(todayTitle);
          const size = getSize(todayTitle);

          pageContent?.scrollTo?.({
            left: offset.left + size.width,
            top: offset.top - size.height,
            behavior: "smooth",
          });
          scroll({ left: offset.left + size.width, top: offset.top - size.height, behavior: "smooth" });
        }
      }, 500);
    }
  }, [JSON.stringify(viewingDate)]);

  return (
    <div id="weeks-container" className={styles.weeksContainer}>
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
            weekEvents={week}
            weekIndex={weekIndex}
            week={days.slice(weekIndex * 7, weekIndex * 7 + 7)}
          />
        ))}
      </div>
    </div>
  );
};
export default Month;
