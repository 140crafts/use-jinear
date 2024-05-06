"use client";
import MainFeaturesSideMenu from "@/components/mainFeaturesSideMenu/MainFeaturesSideMenu";
import WorkspaceLayoutHeader from "@/components/workspaceLayoutHeader/WorkspaceLayoutHeader";
import useWidthLimit from "@/hooks/useWidthLimit";
import { selectWorkspaceFromWorkspaceUsername } from "@/store/slice/accountSlice";
import { closeAllMenus } from "@/store/slice/displayPreferenceSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import isPwa from "@/utils/pwaHelper";
import cn from "classnames";
import { useParams, usePathname } from "next/navigation";
import React, { useEffect } from "react";
import styles from "./layout.module.scss";

interface WorkspaceLayoutProps {
  children: React.ReactNode;
}

// $tablet: 768px;
const MOBILE_LAYOUT_BREAKPOINT = 768;

const WorkspaceLayout: React.FC<WorkspaceLayoutProps> = ({ children }) => {
  const dispatch = useAppDispatch();
  const params = useParams();
  const pathName = usePathname();
  const isMobile = useWidthLimit({ limit: MOBILE_LAYOUT_BREAKPOINT });
  const _isPwa = isPwa();
  const workspaceName = params?.workspaceName as string;
  const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));

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
        <div
          id="workspace-layout-page-side-menu-container"
          className={cn(styles.workspaceSideMenuContainer, _isPwa && styles.workspaceSideMenuContainerPwa)}
        >
          {workspace && <MainFeaturesSideMenu workspace={workspace} />}
        </div>
        <div id="workspace-layout-page-content" className={styles.pageContent}>
          {/* 
          // disabled feature/cgds-301
          <SseProviderWorkspaceActivities workspaceId={workspace?.workspaceId}>
            {workspace ? (
              <SseListenerWorkspaceActivities workspaceId={workspace.workspaceId} workspaceUsername={workspace.username}>
                {children}
              </SseListenerWorkspaceActivities>
            ) : (
              children
              )}
          </SseProviderWorkspaceActivities> */}
          {children}
        </div>
      </div>
    </div>
  );
};

export default WorkspaceLayout;
