import useWidthLimit from "@/hooks/useWidthLimit";
import {selectWorkspaceFromWorkspaceUsername} from "@/store/slice/accountSlice";
import {useTypedSelector} from "@/store";
import React from "react";
import SideMenuFooter from "../sideMenu/sideMenuFooter/SideMenuFooter";
import WorkspaceMoreActionsButton from "../sideMenu/workspaceMoreActionsButton/WorkspaceMoreActionsButton";
import WorkspaceChangeButton from "../workspaceChangeButton/WorkspaceChangeButton";
import WorkspaceUpgradeButton from "../workspaceUpgradeButton/WorkspaceUpgradeButton";
import styles from "./WorkspaceLayoutHeader.module.scss";
import isPwa from "@/util/pwaHelper";
import InstallPwaAppButton from "@/components/installPwaAppButton/InstallPwaAppButton";
import {useParams} from "react-router-dom";

interface WorkspaceLayoutHeaderProps {
}

// $tablet: 768px;
const MOBILE_LAYOUT_BREAKPOINT = 768;

const WorkspaceLayoutHeader: React.FC<WorkspaceLayoutHeaderProps> = ({}) => {
    const {workspaceName} = useParams();
    const isMobile = useWidthLimit({limit: MOBILE_LAYOUT_BREAKPOINT});
    const _isPwa = isPwa();
    const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));

    const upgradeButtonVariant = isMobile ? "ICON" : "FULL";
    const pwiButtonWithLabel = !isMobile;

    return (
        <div className={styles.container}>
            <div className={styles.headerLeftContent}>
                {workspace && (
                    <>
                        <WorkspaceChangeButton currentWorkspace={workspace}/>
                        <WorkspaceMoreActionsButton/>
                        <WorkspaceUpgradeButton workspace={workspace} variant={upgradeButtonVariant}
                                                className={styles.upgradeButton}/>
                    </>
                )}
            </div>
            <div className={styles.headerRightContent}>
                {!_isPwa && <InstallPwaAppButton className={styles.pwiButton} withLabel={pwiButtonWithLabel}/>}
                <SideMenuFooter/>
            </div>
        </div>
    );
};

export default WorkspaceLayoutHeader;
