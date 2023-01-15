import Logger from "@/utils/logger";
import useTranslation from "locales/useTranslation";
import React from "react";
import ActionButtonContainer from "./actionButtonContainer/ActionButtonContainer";
import CurrentWorkspaceHeader from "./currentWorkspaceHeader/CurrentWorkspaceHeader";
import styles from "./SideMenu.module.scss";
import TeamsContainer from "./teamsContainer/TeamsContainer";
import WorkspaceMembers from "./workspaceMembers/WorkspaceMembers";

interface SideMenuProps {}

const logger = Logger("SideMenu");

const SideMenu: React.FC<SideMenuProps> = ({}) => {
  const { t } = useTranslation();
  return (
    <div className={styles.content}>
      <CurrentWorkspaceHeader />
      <WorkspaceMembers />
      <ActionButtonContainer />
      <TeamsContainer />
    </div>
  );
};

export default SideMenu;
