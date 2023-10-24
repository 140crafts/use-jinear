"use client";
import MainFeaturesSideMenu from "@/components/mainFeaturesSideMenu/MainFeaturesSideMenu";
import SideMenuFooter from "@/components/sideMenu/sideMenuFooter/SideMenuFooter";
import Transition from "@/components/transition/Transition";
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
