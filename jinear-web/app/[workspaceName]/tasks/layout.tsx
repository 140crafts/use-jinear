"use client";
import TasksSectionSideMenu from "@/components/tasksSectionSideMenu/TasksSectionSideMenu";
import { selectTasksMenuVisible, toggleTasksMenu } from "@/store/slice/displayPreferenceSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import cn from "classnames";
import React from "react";
import styles from "./layout.module.scss";

import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { LuChevronLeft, LuChevronRight } from "react-icons/lu";

interface TasksLayoutProps {
  children: React.ReactNode;
}

const TasksLayout: React.FC<TasksLayoutProps> = ({ children }) => {
  const dispatch = useAppDispatch();
  const tasksMenuVisible = useTypedSelector(selectTasksMenuVisible);

  const toggleMenu = () => {
    dispatch(toggleTasksMenu());
  };

  const MenuIcon = tasksMenuVisible ? LuChevronLeft : LuChevronRight;
  const menuVariant = tasksMenuVisible ? ButtonVariants.default : ButtonVariants.outline;

  return (
    <div className={styles.container}>
      <div className={cn(styles.sideMenu, tasksMenuVisible && styles.sideMenuVisible)}>
        <div className={styles.sideMenuActionBar}>
          <Button
            className={styles.menuToggleButton}
            heightVariant={ButtonHeight.short}
            onClick={toggleMenu}
            variant={menuVariant}
          >
            <MenuIcon size={14} className={styles.menuToggleIcon} />
          </Button>
        </div>
        {tasksMenuVisible && <TasksSectionSideMenu />}
      </div>

      <div className={styles.contentContainer}>{children}</div>
    </div>
  );
};

export default TasksLayout;
