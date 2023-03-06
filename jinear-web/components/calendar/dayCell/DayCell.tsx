import cn from "classnames";
import { format, isSameMonth } from "date-fns";
import React from "react";
import { useTaskListForDate, useViewingDate } from "../context/CalendarContext";
import styles from "./DayCell.module.css";
import Event from "./event/Event";

interface DayCellProps {
  day: Date;
}

const DayCell: React.FC<DayCellProps> = ({ day }) => {
  const viewingDate = useViewingDate();
  const isCurrentDayAndVieweingDateInDifferentMonth = !isSameMonth(viewingDate, day);
  const tasks = useTaskListForDate(day);
  return (
    <div className={cn(styles.container, isCurrentDayAndVieweingDateInDifferentMonth && styles.differentMonthContainer)}>
      <div className={cn(styles.titleContainer, isCurrentDayAndVieweingDateInDifferentMonth && styles.differentMonthTitle)}>
        {format(day, "dd")}
      </div>
      <div className={styles.taskListContainer}>
        {tasks?.map((task) => (
          <Event key={`day-cell-${day.getTime()}-task-${task.taskId}`} day={day} task={task} />
        ))}
      </div>
    </div>
  );
};

export default DayCell;
