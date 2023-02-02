import TaskOwnerInfo from "@/components/taskDetail/taskOwnerInfo/TaskOwnerInfo";
import cn from "classnames";
import React from "react";
import { useTask } from "../context/TaskDetailContext";
import TaskDescription from "../taskDescription/TaskDescription";
import TaskTitle from "../taskTitle/TaskTitle";
import styles from "./TaskBody.module.css";

interface TaskBodyProps {
  className?: string;
}

const TaskBody: React.FC<TaskBodyProps> = ({ className }) => {
  const task = useTask();
  return (
    <div className={cn(styles.container, className)}>
      <TaskTitle taskId={task.taskId} title={task.title} />
      <TaskDescription taskId={task.taskId} description={task.description} />
      <TaskOwnerInfo owner={task.owner} createdDate={task.createdDate} />
    </div>
  );
};

export default TaskBody;
