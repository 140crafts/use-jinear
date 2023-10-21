"use client";
import React from "react";
import BasicTeamList from "../sideMenu/basicTeamList/BasicTeamList";
import styles from "./TasksSectionSideMenu.module.css";

interface TasksSectionSideMenuProps {}

const TasksSectionSideMenu: React.FC<TasksSectionSideMenuProps> = ({}) => {
  return (
    <div className={styles.container}>
      <BasicTeamList />
    </div>
  );
};

export default TasksSectionSideMenu;
