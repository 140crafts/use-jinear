import Logger from "@/utils/logger";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./SideMenu.module.scss";
import ActionButtonContainer from "./actionButtonContainer/ActionButtonContainer";
import CurrentWorkspaceHeader from "./currentWorkspaceHeader/CurrentWorkspaceHeader";

interface SideMenuProps {}

const logger = Logger("SideMenu");

const SideMenu: React.FC<SideMenuProps> = ({}) => {
  const { t } = useTranslation();
  return (
    <div className={styles.content}>
      <CurrentWorkspaceHeader />
      {/* <WorkspaceMembers /> */}
      <div className="spacer-h-1" />
      <ActionButtonContainer />
      {/* <BasicTeamList /> */}
    </div>
  );
};

export default SideMenu;
