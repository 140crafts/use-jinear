import useWidthLimit from "@/hooks/useWidthLimit";
import { selectWorkspaceFromWorkspaceUsername } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { useParams, useRouter } from "next/navigation";
import React from "react";
import Button, { ButtonHeight } from "../button";
import SideMenuFooter from "../sideMenu/sideMenuFooter/SideMenuFooter";
import WorkspaceMoreActionsButton from "../sideMenu/workspaceMoreActionsButton/WorkspaceMoreActionsButton";
import WorkspaceChangeButton from "../workspaceChangeButton/WorkspaceChangeButton";
import WorkspaceUpgradeButton from "../workspaceUpgradeButton/WorkspaceUpgradeButton";
import styles from "./WorkspaceLayoutHeader.module.scss";

interface WorkspaceLayoutHeaderProps {}
// $tablet: 768px;
const MOBILE_LAYOUT_BREAKPOINT = 768;

const WorkspaceLayoutHeader: React.FC<WorkspaceLayoutHeaderProps> = ({}) => {
  const router = useRouter();
  const params = useParams();
  const isMobile = useWidthLimit({ limit: MOBILE_LAYOUT_BREAKPOINT });
  const workspaceName = params?.workspaceName as string;
  const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));
  const upgradeButtonVariant = isMobile ? "ICON" : "FULL";

  return (
    <div className={styles.container}>
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
  );
};

export default WorkspaceLayoutHeader;
