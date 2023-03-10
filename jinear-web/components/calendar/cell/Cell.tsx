import { TaskDto } from "@/model/be/jinear-core";
import { retrieveTaskStatusIcon } from "@/utils/taskIconFactory";
import cn from "classnames";
import Link from "next/link";
import React from "react";
import { isDateBetween } from "../calendarUtils";
import {
  useHighligtedTaskId,
  useIsDateBetweenViewingPeriod,
  useIsDateFirstDayOfViewingPeriod,
  useIsDateLastDayOfViewingPeriod,
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

  const isDateFirstDayOfViewingPeriod = useIsDateFirstDayOfViewingPeriod(weekStart);
  const isDateLastDayOfViewingPeriod = useIsDateLastDayOfViewingPeriod(weekEnd);
  const isAssignedDateWithinPeriod = useIsDateBetweenViewingPeriod(_assignedDate);
  const isDueDateWithinPeriod = useIsDateBetweenViewingPeriod(_dueDate);

  const isStartDateNotInViewingPeriodAndTodayIsFirstDayOfViewingPeriod =
    _assignedDate && !isAssignedDateWithinPeriod && isDateFirstDayOfViewingPeriod;
  const isEndDateNotInViewingPeriodAndTodayIsLastDayOfViewingPeriod =
    _dueDate && !isDueDateWithinPeriod && isDateLastDayOfViewingPeriod;

  const Icon = retrieveTaskStatusIcon(task?.workflowStatus.workflowStateGroup);
  const isCompleted =
    task &&
    task.workflowStatus.workflowStateGroup &&
    (task.workflowStatus.workflowStateGroup == "COMPLETED" || task.workflowStatus.workflowStateGroup == "CANCELLED");

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
        (isDueDateWithinThisWeek || isOneOfDatesNotSet) && styles.startDayEndDayMargin,
        (isAssignedDateWithinThisWeek || isOneOfDatesNotSet) && styles.startDay,
        (isDueDateWithinThisWeek || isOneOfDatesNotSet) && styles.endDay,
        isCompleted && styles["completed-fill"]
      )}
      style={{ flex: weight }}
      onMouseEnter={_hoverStart}
      onMouseOut={_hoverEnd}
    >
      {isStartDateNotInViewingPeriodAndTodayIsFirstDayOfViewingPeriod && <div className={styles["arrow-right"]}></div>}
      <div className={styles.iconContainer}>{task && <Icon size={17} />}</div>
      <div
        className={cn(styles.title, "line-clamp", isCompleted && styles["title-line-through"])}
        onMouseEnter={_hoverStart}
        onMouseOut={_hoverEnd}
      >
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
