import { useDebouncedEffect } from "@/hooks/useDebouncedEffect";
import { useDebouncedWindowHeight } from "@/hooks/useDebouncedWindowSize";
import { TaskDto } from "@/model/be/jinear-core";
import getCssVariable from "@/utils/cssHelper";
import Logger from "@/utils/logger";
import cn from "classnames";
import { format, isSameMonth } from "date-fns";
import useTranslation from "locales/useTranslation";
import React, { useRef, useState } from "react";
import { useFullSizeDays, useSetFullSizeDays, useViewingDate } from "../context/CalendarContext";
import styles from "./DayCell.module.css";
import Event from "./event/Event";

interface DayCellProps {
  day: Date;
  tasksOnDay: (TaskDto | null)[];
}

const logger = Logger("DayCell");

const DayCell: React.FC<DayCellProps> = ({ day, tasksOnDay }) => {
  const { t } = useTranslation();
  const containerRef = useRef<HTMLDivElement>(null);
  const titleRef = useRef<HTMLDivElement>(null);
  const contentRef = useRef<HTMLDivElement>(null);

  const height = useDebouncedWindowHeight();
  const viewingDate = useViewingDate();
  const fullSizeDays = useFullSizeDays();
  const setFullSizeDays = useSetFullSizeDays();
  const isCurrentDayAndVieweingDateInDifferentMonth = !isSameMonth(viewingDate, day);
  const tasksCount = tasksOnDay?.length || 0;
  const [visibleTaskCount, setVisibleTaskCount] = useState(0);

  useDebouncedEffect(
    () => {
      if (containerRef.current && titleRef.current && contentRef.current) {
        const containerHeight = containerRef.current.offsetHeight;
        const titleHeight = titleRef.current.offsetHeight;
        const eventHeight = parseInt(getCssVariable("--calendar-event-height")?.replace("px", "") || "0");
        const eventGap = parseInt(getCssVariable("--calendar-event-gap")?.replace("px", "") || "0");
        const moreLabelHeight = eventHeight;
        logger.log({ containerHeight, titleHeight, eventHeight });
        const visibleContentHeight = containerHeight - titleHeight - moreLabelHeight;
        const visibleTaskCount = parseInt(`${visibleContentHeight / (eventHeight + eventGap)}`);
        setVisibleTaskCount(visibleTaskCount == Number.NaN ? 0 : visibleTaskCount);
      }
    },
    [containerRef, titleRef, contentRef, height, fullSizeDays, tasksCount, tasksOnDay?.length],
    110
  );

  const unvisibleTaskCount =
    tasksOnDay?.filter((task, index) => index + 1 > visibleTaskCount)?.filter((task) => task != null).length || 0;

  const expandDays = () => {
    setFullSizeDays?.(true);
  };

  return (
    <div ref={containerRef} className={cn(styles.container)}>
      <div
        ref={titleRef}
        className={cn(styles.titleContainer, isCurrentDayAndVieweingDateInDifferentMonth && styles.differentMonthTitle)}
      >
        {format(day, "dd")}
      </div>
      <div ref={contentRef} className={styles.taskListContainer}>
        {tasksOnDay?.map((task, rowIndex) => (
          <Event
            key={`day-cell-${day.getTime()}-row-${rowIndex}-task-${task?.taskId}}`}
            className={rowIndex + 1 > visibleTaskCount ? styles.unvisibleEvent : undefined}
            day={day}
            task={task}
          />
        ))}
        {unvisibleTaskCount != 0 && (
          <div className={styles.hasUnvisibleTasks} onClick={expandDays}>
            <span>
              {unvisibleTaskCount} {t("calendarDayMore")}
            </span>
          </div>
        )}
      </div>
    </div>
  );
};

export default DayCell;
