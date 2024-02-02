"use client";
import { selectCalendarsMenuVisible, toggleCalendarsMenu } from "@/store/slice/displayPreferenceSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import cn from "classnames";
import React from "react";
import styles from "./layout.module.scss";

import CalendarSectionSideMenu from "@/components/calendarSectionSideMenu/CalendarSectionSideMenu";
import SecondLevelSideMenu from "@/components/secondLevelSideMenu/SecondLevelSideMenu";

interface TasksLayoutProps {
  children: React.ReactNode;
}

const CalendarLayout: React.FC<TasksLayoutProps> = ({ children }) => {
  const dispatch = useAppDispatch();
  const calendarsMenuVisible = useTypedSelector(selectCalendarsMenuVisible);

  const toggleMenu = () => {
    dispatch(toggleCalendarsMenu());
  };

  return (
    <div id="calendar-layout-container" className={styles.container}>
      <SecondLevelSideMenu open={calendarsMenuVisible} toggle={toggleMenu} className={styles.secondLevelSideMenu}>
        <CalendarSectionSideMenu />
      </SecondLevelSideMenu>
      <div
        id="calendar-layout-content"
        className={cn(styles.contentContainer, calendarsMenuVisible && styles.contentContainerWithSideMenu)}
      >
        {children}
      </div>
    </div>
  );
};

export default CalendarLayout;
