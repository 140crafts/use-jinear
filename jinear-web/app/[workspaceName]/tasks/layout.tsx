"use client";
import { selectTasksMenuVisible, toggleTasksMenu } from "@/store/slice/displayPreferenceSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import cn from "classnames";
import React from "react";
import styles from "./layout.module.scss";

import SecondLevelSideMenu from "@/components/secondLevelSideMenu/SecondLevelSideMenu";
import TasksSectionSideMenu from "@/components/tasksSectionSideMenu/TasksSectionSideMenu";

interface TasksLayoutProps {
  children: React.ReactNode;
}

const TasksLayout: React.FC<TasksLayoutProps> = ({ children }) => {
  const dispatch = useAppDispatch();
  const tasksMenuVisible = useTypedSelector(selectTasksMenuVisible);

  const toggleMenu = () => {
    dispatch(toggleTasksMenu());
  };

  return (
    <div id="tasks-layout-container" className={styles.container}>
      <SecondLevelSideMenu open={tasksMenuVisible} toggle={toggleMenu} className={styles.secondLevelSideMenu}>
        <TasksSectionSideMenu />
      </SecondLevelSideMenu>
      <div
        id="tasks-layout-content"
        className={cn(styles.contentContainer, tasksMenuVisible && styles.contentContainerWithSideMenu)}
      >
        {children}
      </div>
    </div>
  );
};

export default TasksLayout;
