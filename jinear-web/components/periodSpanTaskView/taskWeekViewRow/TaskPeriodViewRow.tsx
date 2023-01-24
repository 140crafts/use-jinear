import { TaskDto } from "@/model/be/jinear-core";
import getCssVariable from "@/utils/cssHelper";
import Logger from "@/utils/logger";
import {
  differenceInDays,
  differenceInHours,
  isAfter,
  isBefore,
} from "date-fns";
import React from "react";
import {
  usePeriodEnd,
  usePeriodStart,
  useVariant,
} from "../context/PeriodSpanTaskViewContext";
import TaskPeriodViewCard from "../taskPeriodViewCard/TaskPeriodViewCard";
import styles from "./TaskPeriodViewRow.module.css";

interface TaskPeriodViewRowProps {
  task: TaskDto;
}
const logger = Logger("TaskPeriodViewRow");

const calcFlexes = (vo: {
  viewingPeriodStart: Date;
  viewingPeriodEnd: Date;
  task: TaskDto;
}) => {
  const { viewingPeriodStart, viewingPeriodEnd, task } = vo;
  const DIFF = Math.ceil(
    differenceInHours(viewingPeriodEnd, viewingPeriodStart) / 24
  );
  logger.log({ DIFF });

  const leftSpacerFlex = isBefore(
    new Date(task.assignedDate),
    viewingPeriodStart
  )
    ? 0
    : differenceInDays(new Date(task.assignedDate), viewingPeriodStart);

  let taskFlex = 0;
  let rightSpacerFlex = 0;

  if (task.dueDate == null) {
    taskFlex = 1;
    rightSpacerFlex = DIFF - taskFlex - leftSpacerFlex;
  } else {
    if (isAfter(new Date(task.dueDate), viewingPeriodEnd)) {
      taskFlex = DIFF - leftSpacerFlex;
      rightSpacerFlex = 0;
    } else {
      rightSpacerFlex = Math.abs(
        differenceInDays(viewingPeriodEnd, new Date(task.dueDate))
      );
      taskFlex = DIFF - leftSpacerFlex - rightSpacerFlex;
    }
  }
  return { leftSpacerFlex, taskFlex, rightSpacerFlex };
};

const TaskPeriodViewRow: React.FC<TaskPeriodViewRowProps> = ({ task }) => {
  const viewingPeriodStart = usePeriodStart() || new Date();
  const viewingPeriodEnd = usePeriodEnd() || new Date();
  const variant = useVariant() || "week";
  const { leftSpacerFlex, taskFlex, rightSpacerFlex } = calcFlexes({
    viewingPeriodStart,
    viewingPeriodEnd,
    task,
  });

  const taskCardWidth = parseInt(
    getCssVariable(`--task-card-width-${variant}`)?.replace("px", "")
  );

  return (
    <div className={styles.container}>
      <div
        className={styles.spacer}
        style={{
          flex: leftSpacerFlex,
          display: leftSpacerFlex == 0 ? "none" : "inherit",
          minWidth: taskCardWidth * leftSpacerFlex,
        }}
      />
      <TaskPeriodViewCard
        key={`twvc-${task.taskId}-${viewingPeriodStart.toISOString()}`}
        style={{ flex: taskFlex, minWidth: taskCardWidth * taskFlex }}
        task={task}
        duration={taskFlex}
        isStartDateBefore={isBefore(
          new Date(task.assignedDate),
          viewingPeriodStart
        )}
        isDueDateAfter={
          task.dueDate && isAfter(new Date(task.dueDate), viewingPeriodEnd)
        }
        showAdditionalInfo={variant == "week"}
      />
      <div
        className={styles.spacer}
        style={{
          flex: rightSpacerFlex,
          display: rightSpacerFlex == 0 ? "none" : "inherit",
          minWidth: taskCardWidth * rightSpacerFlex,
        }}
      />
    </div>
  );
};

export default TaskPeriodViewRow;
