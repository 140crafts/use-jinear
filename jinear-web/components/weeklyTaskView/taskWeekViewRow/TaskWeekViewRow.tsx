import { TaskDto } from "@/model/be/jinear-core";
import Logger from "@/utils/logger";
import { differenceInDays, isAfter, isBefore } from "date-fns";
import React from "react";
import TaskWeekViewCard from "../taskWeekViewCard/TaskWeekViewCard";
import styles from "./TaskWeekViewRow.module.css";

interface TaskWeekViewRowProps {
  viewingWeekStart: Date;
  viewingWeekEnd: Date;
  task: TaskDto;
}
const logger = Logger("TaskWeekViewRow");

const calcFlexes = (vo: {
  viewingWeekStart: Date;
  viewingWeekEnd: Date;
  task: TaskDto;
}) => {
  const { viewingWeekStart, viewingWeekEnd, task } = vo;
  const leftSpacerFlex = isBefore(new Date(task.assignedDate), viewingWeekStart)
    ? 0
    : differenceInDays(new Date(task.assignedDate), viewingWeekStart);

  let taskFlex = 0;
  let rightSpacerFlex = 0;

  if (task.dueDate == null) {
    taskFlex = 1;
    rightSpacerFlex = 7 - taskFlex - leftSpacerFlex;
  } else {
    if (isAfter(new Date(task.dueDate), viewingWeekEnd)) {
      taskFlex = 7 - leftSpacerFlex;
      rightSpacerFlex = 0;
    } else {
      rightSpacerFlex = Math.abs(
        differenceInDays(viewingWeekEnd, new Date(task.dueDate))
      );
      taskFlex = 7 - leftSpacerFlex - rightSpacerFlex;
    }
  }
  return { leftSpacerFlex, taskFlex, rightSpacerFlex };
};

const TaskWeekViewRow: React.FC<TaskWeekViewRowProps> = ({
  viewingWeekStart,
  viewingWeekEnd,
  task,
}) => {
  const { leftSpacerFlex, taskFlex, rightSpacerFlex } = calcFlexes({
    viewingWeekStart,
    viewingWeekEnd,
    task,
  });

  return (
    <div className={styles.container}>
      <div
        className={styles.spacer}
        style={{
          flex: leftSpacerFlex,
          display: leftSpacerFlex == 0 ? "none" : "inherit",
        }}
      />
      <TaskWeekViewCard
        key={`twvc-${task.taskId}-${viewingWeekStart.toISOString()}`}
        className={styles.taskCard}
        style={{ flex: taskFlex }}
        task={task}
        daysInThisWeek={taskFlex}
        isStartDateBefore={isBefore(
          new Date(task.assignedDate),
          viewingWeekStart
        )}
        isDueDateAfter={
          task.dueDate && isAfter(new Date(task.dueDate), viewingWeekEnd)
        }
      />
      <div
        className={styles.spacer}
        style={{
          flex: rightSpacerFlex,
          display: rightSpacerFlex == 0 ? "none" : "inherit",
        }}
      />
    </div>
  );
};

export default TaskWeekViewRow;
