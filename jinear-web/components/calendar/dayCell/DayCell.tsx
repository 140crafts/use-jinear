import { TaskDto } from "@/model/be/jinear-core";
import cn from "classnames";
import { format, isSameMonth } from "date-fns";
import React from "react";
import { useViewingDate } from "../context/CalendarContext";
import styles from "./DayCell.module.css";
import Event from "./event/Event";

interface DayCellProps {
  day: Date;
  tasksOnDay: (TaskDto | null)[];
}

const DayCell: React.FC<DayCellProps> = ({ day, tasksOnDay }) => {
  const viewingDate = useViewingDate();
  const isCurrentDayAndVieweingDateInDifferentMonth = !isSameMonth(viewingDate, day);

  return (
    <div className={cn(styles.container)}>
      <div className={cn(styles.titleContainer, isCurrentDayAndVieweingDateInDifferentMonth && styles.differentMonthTitle)}>
        {format(day, "dd")}
      </div>
      <div className={styles.taskListContainer}>
        {tasksOnDay?.map((task, rowIndex) => (
          <Event key={`day-cell-${day.getTime()}-row-${rowIndex}-task-${task?.taskId}}`} day={day} task={task} />
        ))}
      </div>
    </div>
  );
};

export default DayCell;
