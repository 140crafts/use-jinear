import { TaskDto } from "@/model/be/jinear-core";
import cn from "classnames";
import Link from "next/link";
import React from "react";
import { isDateBetween } from "../calendarUtils";
import {
  isDateBetweenViewingPeriod,
  isDateFirstDayOfViewingPeriod,
  isDateLastDayOfViewingPeriod,
  useHighligtedTaskId,
  useSetHighlightedTaskId,
} from "../context/CalendarContext";
import styles from "./Cell.module.css";

interface CellProps {
  weight: number;
  task?: TaskDto | null;
  id: string;
  weekStart: Date;
  weekEnd: Date;
}

const Cell: React.FC<CellProps> = ({ id, weight, task, weekStart, weekEnd }) => {
  const highlightedTaskId = useHighligtedTaskId();
  const setHighlightedTaskId = useSetHighlightedTaskId();
  const highlighted = task && highlightedTaskId == task.taskId;

  const _assignedDate = task?.assignedDate && new Date(task.assignedDate);
  const _dueDate = task?.dueDate && new Date(task.dueDate);
  const isAssignedDateWithinThisWeek = _assignedDate && isDateBetween(weekStart, _assignedDate, weekEnd);
  const isDueDateWithinThisWeek = _dueDate && isDateBetween(weekStart, _dueDate, weekEnd);
  const isOneOfDatesNotSet = !_assignedDate || !_dueDate;

  const isStartDateNotInViewingPeriodAndTodayIsFirstDayOfViewingPeriod =
    _assignedDate && !isDateBetweenViewingPeriod(_assignedDate) && isDateFirstDayOfViewingPeriod(weekStart);
  const isEndDateNotInViewingPeriodAndTodayIsLastDayOfViewingPeriod =
    _dueDate && !isDateBetweenViewingPeriod(_dueDate) && isDateLastDayOfViewingPeriod(weekEnd);

  const _hoverStart = () => {
    if (task) {
      setHighlightedTaskId?.(task.taskId);
    }
  };

  const _hoverEnd = () => {
    setHighlightedTaskId?.("");
  };

  return (
    <Link
      tabIndex={task ? undefined : -1}
      href={`/${task?.workspace?.username}/task/${task?.team?.tag}-${task?.teamTagNo}`}
      id={id}
      className={cn(
        styles.container,
        task && styles.fill,
        highlighted && styles.highlight,
        (isAssignedDateWithinThisWeek || isOneOfDatesNotSet) && styles.startDay,
        (isDueDateWithinThisWeek || isOneOfDatesNotSet) && styles.endDay
      )}
      style={{ flex: weight }}
      onMouseEnter={_hoverStart}
      onMouseOut={_hoverEnd}
    >
      {isStartDateNotInViewingPeriodAndTodayIsFirstDayOfViewingPeriod && <div className={styles["arrow-right"]}></div>}
      <div className={cn(styles.title, "line-clamp")} onMouseEnter={_hoverStart} onMouseOut={_hoverEnd}>
        {task?.title}
      </div>
      {isEndDateNotInViewingPeriodAndTodayIsLastDayOfViewingPeriod && (
        <div className={styles["arrow-right-end-bg"]}>
          <div className={cn(styles["arrow-right-end"], highlighted && styles["arrow-right-end-highlight"])}></div>
        </div>
      )}
    </Link>
  );
};

export default Cell;
