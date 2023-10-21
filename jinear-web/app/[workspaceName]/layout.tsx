"use client";
import MainFeaturesSideMenu from "@/components/mainFeaturesSideMenu/MainFeaturesSideMenu";
import SideMenuFooter from "@/components/sideMenu/sideMenuFooter/SideMenuFooter";
import Transition from "@/components/transition/Transition";
import React from "react";
import styles from "./layout.module.scss";

interface WorkspaceLayoutProps {
  children: React.ReactNode;
}

const WorkspaceLayout: React.FC<WorkspaceLayoutProps> = ({ children }) => {
  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <div>TODO Current Workspace</div>
        <SideMenuFooter />
      </div>
      <div className={styles.content}>
        <div className={styles.workspaceSideMenuContainer}>
          <MainFeaturesSideMenu />
        </div>
        <Transition>
          <div className={styles.pageContent}>{children}</div>
        </Transition>
      </div>
    </div>
  );
};

export default WorkspaceLayout;
