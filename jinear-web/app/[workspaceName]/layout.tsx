"use client";
import MainFeaturesSideMenu from "@/components/mainFeaturesSideMenu/MainFeaturesSideMenu";
import WorkspaceLayoutHeader from "@/components/workspaceLayoutHeader/WorkspaceLayoutHeader";
import useWidthLimit from "@/hooks/useWidthLimit";
import { closeAllMenus } from "@/store/slice/displayPreferenceSlice";
import { useAppDispatch } from "@/store/store";
import { usePathname } from "next/navigation";
import React, { useEffect } from "react";
import styles from "./layout.module.scss";

interface WorkspaceLayoutProps {
  children: React.ReactNode;
}

// $tablet: 768px;
const MOBILE_LAYOUT_BREAKPOINT = 768;

const WorkspaceLayout: React.FC<WorkspaceLayoutProps> = ({ children }) => {
  const dispatch = useAppDispatch();
  const pathName = usePathname();
  const isMobile = useWidthLimit({ limit: MOBILE_LAYOUT_BREAKPOINT });

  useEffect(() => {
    if (isMobile) {
      dispatch(closeAllMenus());
    }
  }, [pathName, isMobile]);

  return (
    <div id="workspace-layout-container" className={styles.container}>
      <div id="workspace-layout-header" className={styles.header}>
        <WorkspaceLayoutHeader />
      </div>
      <div id="workspace-layout-content" className={styles.content}>
        <div id="workspace-layout-page-side-menu-container" className={styles.workspaceSideMenuContainer}>
          <MainFeaturesSideMenu />
        </div>
        <div id="workspace-layout-page-content" className={styles.pageContent}>
          {children}
        </div>
      </div>
    </div>
  );
};

export default WorkspaceLayout;
