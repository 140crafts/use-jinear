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
import { useLazyRetrieveChannelMembershipsQuery, useRetrieveChannelMembershipsQuery } from "@/api/channelMemberApi";
import {
  useLazyRetrieveParticipatedConversationsQuery,
  useRetrieveParticipatedConversationsQuery
} from "@/api/conversationApi";
import useDetectKeyboardOpen from "@/hooks/useDetectKeyboardOpen";
import { usePageVisibility } from "@/hooks/usePageVisibility";
import { useFeatureFlag } from "@/hooks/useFeatureFlag";

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
  const onScreenKeyboardOpen = useDetectKeyboardOpen();
  const _isPwa = isPwa();
  const workspaceName = params?.workspaceName as string;
  const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));
  const pageVisibility = usePageVisibility();
  const messagingEnabled = useFeatureFlag("MESSAGING");

  //so we can calculate unread count
  const [retrieveChannelMembershipsQuery] = useLazyRetrieveChannelMembershipsQuery();
  const [retrieveParticipatedConversationsQuery] = useLazyRetrieveParticipatedConversationsQuery();

  useEffect(() => {
    if (pageVisibility && workspace && pageVisibility) {
      retrieveChannelMembershipsQuery({ workspaceId: workspace.workspaceId });
      messagingEnabled && retrieveParticipatedConversationsQuery({ workspaceId: workspace.workspaceId });
    }
  }, [retrieveChannelMembershipsQuery, retrieveParticipatedConversationsQuery, workspace, pageVisibility, messagingEnabled]);

  useEffect(() => {
    if (isMobile) {
      dispatch(closeAllMenus());
    }
  }, [dispatch, pathName, isMobile]);

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
          {children}
        </div>
      </div>
    </div>
  );
};

export default WorkspaceLayout;
