"use client";
import Button, { ButtonHeight } from "@/components/button";
import MainFeaturesSideMenu from "@/components/mainFeaturesSideMenu/MainFeaturesSideMenu";
import SideMenuFooter from "@/components/sideMenu/sideMenuFooter/SideMenuFooter";
import Transition from "@/components/transition/Transition";
import WorkspaceChangeButton from "@/components/workspaceChangeButton/WorkspaceChangeButton";
import WorkspaceUpgradeButton from "@/components/workspaceUpgradeButton/WorkspaceUpgradeButton";
import useWidthLimit from "@/hooks/useWidthLimit";
import { selectWorkspaceFromWorkspaceUsername } from "@/store/slice/accountSlice";
import { closeAllMenus } from "@/store/slice/displayPreferenceSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import { useParams, usePathname, useRouter } from "next/navigation";
import React, { useEffect } from "react";
import { LuSettings, LuUsers2 } from "react-icons/lu";
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

  useEffect(() => {
    if (isMobile) {
      dispatch(closeAllMenus());
    }
  }, [pathName, isMobile]);

  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <Button heightVariant={ButtonHeight.short} onClick={router.back}>
          {"<-"}
        </Button>
        {workspace && <WorkspaceChangeButton currentWorkspace={workspace} />}
        {workspace && <WorkspaceUpgradeButton workspace={workspace} variant={"ICON"} className={styles.upgradeButton} />}
        <Button>
          <LuUsers2 />
        </Button>
        <Button>
          <LuSettings />
        </Button>
        <div className="flex-1"></div>
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
