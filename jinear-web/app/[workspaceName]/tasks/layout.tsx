"use client";
import TasksSectionSideMenu from "@/components/tasksSectionSideMenu/TasksSectionSideMenu";
import React from "react";
import styles from "./layout.module.css";

interface TasksLayoutProps {
  children: React.ReactNode;
}

const TasksLayout: React.FC<TasksLayoutProps> = ({ children }) => {
  return (
    <div className={styles.container}>
      <div className={styles.tasksSideMenuContainer}>
        <TasksSectionSideMenu />
      </div>
      <div className={styles.contentContainer}>{children}</div>
    </div>
  );
};

export default TasksLayout;
