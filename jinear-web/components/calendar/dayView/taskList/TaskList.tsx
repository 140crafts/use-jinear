import TaskRow from "@/components/taskLists/taskRow/TaskRow";
import { TaskDto } from "@/model/be/jinear-core";
import Logger from "@/utils/logger";
import cn from "classnames";
import React from "react";
import { useViewingDate } from "../../context/CalendarContext";
import styles from "./TaskList.module.css";

interface TaskListProps {
  className: string;
  viewingDayTasks: TaskDto[];
}

const logger = Logger("TaskList");
const TaskList: React.FC<TaskListProps> = ({ className, viewingDayTasks }) => {
  const viewingDate = useViewingDate();
  logger.log({ viewingDate, viewingDayTasks });
  return (
    <div className={cn(styles.container, className)}>
      {viewingDayTasks.map((task) => (
        <TaskRow task={task} />
      ))}
    </div>
  );
};

export default TaskList;
