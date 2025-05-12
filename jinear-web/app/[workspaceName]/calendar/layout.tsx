"use client";
import cn from "classnames";
import React from "react";
import styles from "./layout.module.scss";

import CalendarSectionSideMenu from "@/components/calendarSectionSideMenu/CalendarSectionSideMenu";
import SecondLevelSideMenuV2 from "@/components/secondLevelSideMenuV2/SecondLevelSideMenuV2";

interface TasksLayoutProps {
  children: React.ReactNode;
}

const CalendarLayout: React.FC<TasksLayoutProps> = ({ children }) => {

  return (
    <div id="calendar-layout-container" className={styles.container}>
      <SecondLevelSideMenuV2>
        <CalendarSectionSideMenu />
      </SecondLevelSideMenuV2>
      <div
        id="calendar-layout-content"
        className={cn(styles.contentContainer, styles.contentContainerWithSideMenu)}
      >
        {children}
      </div>
    </div>
  );
};

export default CalendarLayout;
