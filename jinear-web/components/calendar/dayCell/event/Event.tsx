import { TaskDto } from "@/model/be/jinear-core";
import cn from "classnames";
import { isSameDay } from "date-fns";
import React from "react";
import {
  countAllTasksAtDateBeforeTaskDate,
  isDateBetweenViewingPeriod,
  isDateFirstDayOfViewingPeriod,
} from "../../context/CalendarContext";
import styles from "./Event.module.css";

interface EventProps {
  task: TaskDto;
  day: Date;
}

const Event: React.FC<EventProps> = ({ task, day }) => {
  const _assignedDate = task.assignedDate && new Date(task.assignedDate);
  const _dueDate = task.dueDate && new Date(task.dueDate);
  const isTodayAssignedDate = _assignedDate && isSameDay(_assignedDate, day);
  const isTodayDueDate = _dueDate && isSameDay(_dueDate, day);
  const isOneOfDatesNotSet = !_assignedDate || !_dueDate;

  const isStartDateNotInViewingPeriod =
    _assignedDate && !isDateBetweenViewingPeriod(_assignedDate) && isDateFirstDayOfViewingPeriod(day);

  const taskDate = _assignedDate ? _assignedDate : _dueDate;
  const taskCountBeforeThisTask = countAllTasksAtDateBeforeTaskDate(day, taskDate);

  return (
    <div
      className={cn(
        styles.container,
        (isTodayAssignedDate || isOneOfDatesNotSet) && styles.startDay,
        (isTodayDueDate || isOneOfDatesNotSet) && styles.endDay
      )}
    >
      {(isTodayAssignedDate || isOneOfDatesNotSet || isStartDateNotInViewingPeriod) && (
        <div className="line-clamp">{task.title}</div>
      )}
      <div className="line-clamp">{taskCountBeforeThisTask}</div>
    </div>
  );
};

export default Event;
