import { useDebouncedEffect } from "@/hooks/useDebouncedEffect";
import useWindowSize from "@/hooks/useWindowSize";
import { TaskDto } from "@/model/be/jinear-core";
import getCssVariable from "@/utils/cssHelper";
import Logger from "@/utils/logger";
import cn from "classnames";
import { format, isSameMonth } from "date-fns";
import React, { useRef } from "react";
import { useViewingDate } from "../context/CalendarContext";
import styles from "./DayCell.module.css";
import Event from "./event/Event";

interface DayCellProps {
  day: Date;
  tasksOnDay: (TaskDto | null)[];
}

const logger = Logger("DayCell");

const DayCell: React.FC<DayCellProps> = ({ day, tasksOnDay }) => {
  const containerRef = useRef<HTMLDivElement>(null);
  const { height } = useWindowSize();
  const viewingDate = useViewingDate();
  const isCurrentDayAndVieweingDateInDifferentMonth = !isSameMonth(viewingDate, day);
  const tasksCount = tasksOnDay.length;

  useDebouncedEffect(
    () => {
      if (containerRef.current) {
        const height = containerRef.current.offsetHeight;
        const eventHeight = parseInt(getCssVariable("--calendar-event-height")?.replace("px", "") || "0");
        const eventGap = parseInt(getCssVariable("--calendar-event-gap")?.replace("px", "") || "0");
        const eventTotalHeight = eventHeight + eventGap;
        const totalContentHeight = eventTotalHeight * tasksCount;
        const contentContainerHeightDiff = totalContentHeight - height;
        const visibleCount =
          contentContainerHeightDiff > 0 ? tasksCount - parseInt(`${contentContainerHeightDiff / eventTotalHeight}`) : tasksCount;
        logger.log({ height, eventTotalHeight, totalContentHeight, visibleCount });
      }
    },
    [height, tasksCount, containerRef],
    1000
  );

  return (
    <div className={cn(styles.container)}>
      <div className={cn(styles.titleContainer, isCurrentDayAndVieweingDateInDifferentMonth && styles.differentMonthTitle)}>
        {format(day, "dd")}
      </div>
      <div ref={containerRef} className={styles.taskListContainer}>
        {tasksOnDay?.map((task, rowIndex) => (
          <Event key={`day-cell-${day.getTime()}-row-${rowIndex}-task-${task?.taskId}}`} day={day} task={task} />
        ))}
      </div>
    </div>
  );
};

export default DayCell;
