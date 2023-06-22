import Logger from "@/utils/logger";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./SideMenu.module.scss";
import ActionButtonContainer from "./actionButtonContainer/ActionButtonContainer";
import BasicTeamList from "./basicTeamList/BasicTeamList";
import CurrentWorkspaceHeader from "./currentWorkspaceHeader/CurrentWorkspaceHeader";
import WorkspaceMembers from "./workspaceMembers/WorkspaceMembers";

interface SideMenuProps {}

const logger = Logger("SideMenu");

const SideMenu: React.FC<SideMenuProps> = ({}) => {
  const { t } = useTranslation();
  return (
    <div className={styles.content}>
      <CurrentWorkspaceHeader />
      <div className="spacer-h-1" />
      <WorkspaceMembers />
      <div className="spacer-h-1" />
      <ActionButtonContainer />
      <BasicTeamList />
    </div>
  );
};

export default SideMenu;
