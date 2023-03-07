import { TaskDto } from "@/model/be/jinear-core";
import cn from "classnames";
import { isSameDay } from "date-fns";
import React from "react";
import {
  isDateBetweenViewingPeriod,
  isDateFirstDayOfViewingPeriod,
  useHighligtedTaskId,
  useSetHighlightedTaskId,
} from "../../context/CalendarContext";
import styles from "./Event.module.css";

interface EventProps {
  task?: TaskDto | null;
  day: Date;
}

const Event: React.FC<EventProps> = ({ task, day }) => {
  const highlightedTaskId = useHighligtedTaskId();
  const setHighlightedTaskId = useSetHighlightedTaskId();
  const _assignedDate = task?.assignedDate && new Date(task.assignedDate);
  const _dueDate = task?.dueDate && new Date(task.dueDate);
  const isTodayAssignedDate = _assignedDate && isSameDay(_assignedDate, day);
  const isTodayDueDate = _dueDate && isSameDay(_dueDate, day);
  const isOneOfDatesNotSet = !_assignedDate || !_dueDate;

  const isStartDateNotInViewingPeriod =
    _assignedDate && !isDateBetweenViewingPeriod(_assignedDate) && isDateFirstDayOfViewingPeriod(day);

  const _hoverStart = () => {
    if (task) {
      setHighlightedTaskId?.(task.taskId);
    }
  };

  const _hoverEnd = () => {
    setHighlightedTaskId?.("");
  };

  return (
    <div
      className={cn(
        styles.container,
        task && styles.fill,
        (isTodayAssignedDate || isOneOfDatesNotSet) && styles.startDay,
        (isTodayDueDate || isOneOfDatesNotSet) && styles.endDay,
        task && highlightedTaskId == task.taskId && styles.highlight
      )}
      onMouseEnter={_hoverStart}
      onMouseOut={_hoverEnd}
    >
      {(isTodayAssignedDate || isOneOfDatesNotSet || isStartDateNotInViewingPeriod) && (
        <div onMouseEnter={_hoverStart} onMouseOut={_hoverEnd} className={cn(styles.title, "line-clamp")}>
          {task?.title}
        </div>
      )}
    </div>
  );
};

export default Event;
