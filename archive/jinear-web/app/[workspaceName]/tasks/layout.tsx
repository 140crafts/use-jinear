"use client";
import cn from "classnames";
import React from "react";
import styles from "./layout.module.scss";
import TasksSectionSideMenu from "@/components/tasksSectionSideMenu/TasksSectionSideMenu";
import SecondLevelSideMenuV2 from "@/components/secondLevelSideMenuV2/SecondLevelSideMenuV2";

interface TasksLayoutProps {
  children: React.ReactNode;
}

const TasksLayout: React.FC<TasksLayoutProps> = ({ children }) => {

  return (
    <div id="tasks-layout-container" className={styles.container}>
      <SecondLevelSideMenuV2>
        <TasksSectionSideMenu />
      </SecondLevelSideMenuV2>
      <div
        id="tasks-layout-content"
        className={cn(styles.contentContainer, styles.contentContainerWithSideMenu)}
      >
        {children}
      </div>
    </div>
  );
};

export default TasksLayout;
