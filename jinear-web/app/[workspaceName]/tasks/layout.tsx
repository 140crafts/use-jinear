"use client";
import { selectTasksMenuVisible, toggleTasksMenu } from "@/store/slice/displayPreferenceSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import React from "react";
import styles from "./layout.module.scss";

import SecondLevelSideMenu from "@/components/secondLevelSideMenu/SecondLevelSideMenu";
import TasksSectionSideMenu from "@/components/tasksSectionSideMenu/TasksSectionSideMenu";
import useTranslation from "locales/useTranslation";

interface TasksLayoutProps {
  children: React.ReactNode;
}

const TasksLayout: React.FC<TasksLayoutProps> = ({ children }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const tasksMenuVisible = useTypedSelector(selectTasksMenuVisible);

  const toggleMenu = () => {
    dispatch(toggleTasksMenu());
  };

  return (
    <div className={styles.container}>
      {/* <div className={cn(styles.sideMenu, tasksMenuVisible && styles.sideMenuVisible)}>
        <div className={styles.sideMenuActionBar}>
          <Button
            className={styles.menuToggleButton}
            heightVariant={ButtonHeight.short}
            onClick={toggleMenu}
            variant={menuVariant}
          >
            <div className={cn(styles.sideMenuCollapsedLabel, !tasksMenuVisible && styles.sideMenuCollapsedLabelClosed)}>
              {t("tasksLayoutSideMenuCollapsedLabel")}
            </div>
            <MenuIcon size={14} className={styles.menuToggleIcon} />
          </Button>
        </div>
        {tasksMenuVisible && <TasksSectionSideMenu />}
      </div> */}
      <SecondLevelSideMenu open={tasksMenuVisible} toggle={toggleMenu}>
        <TasksSectionSideMenu />
      </SecondLevelSideMenu>
      <div className={styles.contentContainer}>{children}</div>
    </div>
  );
};

export default TasksLayout;
