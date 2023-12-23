import TaskRow from "@/components/taskRow/TaskRow";
import { TaskDto } from "@/model/be/jinear-core";
import Logger from "@/utils/logger";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React from "react";
import { useViewingDate } from "../../context/CalendarContext";
import styles from "./TaskList.module.css";

interface TaskListProps {
  className: string;
  viewingDayTasks: TaskDto[];
}

const logger = Logger("TaskList");
const TaskList: React.FC<TaskListProps> = ({ className, viewingDayTasks }) => {
  const { t } = useTranslation();
  const viewingDate = useViewingDate();
  logger.log({ viewingDate, viewingDayTasks });
  return (
    <div className={cn(styles.container, className)}>
      {viewingDayTasks.map((task) => (
        <TaskRow key={task.taskId} task={task} />
      ))}
      {viewingDayTasks.length == 0 && <div className={styles.emptyLabel}>{t("calendarDayViewEmptyDayLabel")}</div>}
    </div>
  );
};

export default TaskList;
