import useWidthLimit from "@/hooks/useWidthLimit";
import { selectWorkspaceFromWorkspaceUsername } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { useParams, useRouter } from "next/navigation";
import React from "react";
import Button, { ButtonHeight, ButtonVariants } from "../button";
import SideMenuFooter from "../sideMenu/sideMenuFooter/SideMenuFooter";
import WorkspaceMoreActionsButton from "../sideMenu/workspaceMoreActionsButton/WorkspaceMoreActionsButton";
import WorkspaceChangeButton from "../workspaceChangeButton/WorkspaceChangeButton";
import WorkspaceUpgradeButton from "../workspaceUpgradeButton/WorkspaceUpgradeButton";
import styles from "./WorkspaceLayoutHeader.module.scss";
import { IoArrowBack } from "react-icons/io5";
import isPwa from "@/utils/pwaHelper";
import InstallPwaAppButton from "@/components/installPwaAppButton/InstallPwaAppButton";

interface WorkspaceLayoutHeaderProps {
}

// $tablet: 768px;
const MOBILE_LAYOUT_BREAKPOINT = 768;

const WorkspaceLayoutHeader: React.FC<WorkspaceLayoutHeaderProps> = ({}) => {
  const router = useRouter();
  const params = useParams();
  const isMobile = useWidthLimit({ limit: MOBILE_LAYOUT_BREAKPOINT });
  const _isPwa = isPwa();
  const workspaceName = params?.workspaceName as string;
  const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));

  const upgradeButtonVariant = isMobile ? "ICON" : "FULL";
  const pwiButtonWithLabel = !isMobile;

  return (
    <div className={styles.container}>
      <div className={styles.headerLeftContent}>
        <Button className={styles.goBackButton} variant={ButtonVariants.hoverFilled2} heightVariant={ButtonHeight.short} onClick={router.back}>
          <b><IoArrowBack /></b>
        </Button>
        {workspace && (
          <>
            <WorkspaceChangeButton currentWorkspace={workspace} />
            <WorkspaceMoreActionsButton />
            <WorkspaceUpgradeButton workspace={workspace} variant={upgradeButtonVariant}
                                    className={styles.upgradeButton} />
          </>
        )}
      </div>
      <div className={styles.headerRightContent}>
        {!_isPwa && <InstallPwaAppButton className={styles.pwiButton} withLabel={pwiButtonWithLabel} />}
        <SideMenuFooter />
      </div>
    </div>
  );
};

export default WorkspaceLayoutHeader;
