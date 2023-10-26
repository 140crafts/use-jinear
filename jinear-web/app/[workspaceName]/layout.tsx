"use client";
import Button, { ButtonHeight } from "@/components/button";
import MainFeaturesSideMenu from "@/components/mainFeaturesSideMenu/MainFeaturesSideMenu";
import SideMenuFooter from "@/components/sideMenu/sideMenuFooter/SideMenuFooter";
import WorkspaceMoreActionsButton from "@/components/sideMenu/workspaceMoreActionsButton/WorkspaceMoreActionsButton";
import WorkspaceChangeButton from "@/components/workspaceChangeButton/WorkspaceChangeButton";
import WorkspaceUpgradeButton from "@/components/workspaceUpgradeButton/WorkspaceUpgradeButton";
import useWidthLimit from "@/hooks/useWidthLimit";
import { selectWorkspaceFromWorkspaceUsername } from "@/store/slice/accountSlice";
import { closeAllMenus } from "@/store/slice/displayPreferenceSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import { useParams, usePathname, useRouter } from "next/navigation";
import React, { useEffect } from "react";
import styles from "./layout.module.scss";

interface WorkspaceLayoutProps {
  children: React.ReactNode;
}

// $tablet: 768px;
const MOBILE_LAYOUT_BREAKPOINT = 768;

const WorkspaceLayout: React.FC<WorkspaceLayoutProps> = ({ children }) => {
  const router = useRouter();
  const dispatch = useAppDispatch();
  const pathName = usePathname();
  const params = useParams();
  const isMobile = useWidthLimit({ limit: MOBILE_LAYOUT_BREAKPOINT });
  const workspaceName = params?.workspaceName as string;
  const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));
  const upgradeButtonVariant = isMobile ? "ICON" : "FULL";

  useEffect(() => {
    if (isMobile) {
      dispatch(closeAllMenus());
    }
  }, [pathName, isMobile]);

  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <div className={styles.headerLeftContent}>
          <Button heightVariant={ButtonHeight.short} onClick={router.back}>
            <b>{"<-"}</b>
          </Button>
          {workspace && (
            <>
              <WorkspaceChangeButton currentWorkspace={workspace} />
              <WorkspaceMoreActionsButton />
              <WorkspaceUpgradeButton workspace={workspace} variant={upgradeButtonVariant} className={styles.upgradeButton} />
            </>
          )}
        </div>
        <div className={styles.headerRightContent}>
          <SideMenuFooter />
        </div>
      </div>
      <div className={styles.content}>
        <div className={styles.workspaceSideMenuContainer}>
          <MainFeaturesSideMenu />
        </div>
        <div className={styles.pageContent}>{children}</div>
      </div>
    </div>
  );
};

export default WorkspaceLayout;
