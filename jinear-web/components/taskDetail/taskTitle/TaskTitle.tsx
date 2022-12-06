import React from "react";
import styles from "./TaskTitle.module.css";

interface TaskTitleProps {
  title: string;
}

const TaskTitle: React.FC<TaskTitleProps> = ({ title }) => {
  return <div className={styles.container}>{title}</div>;
};

export default TaskTitle;
