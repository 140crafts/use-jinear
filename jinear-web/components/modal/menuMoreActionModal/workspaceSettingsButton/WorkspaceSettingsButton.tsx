import Button from "@/components/button";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React from "react";
import { LuSettings } from "react-icons/lu";
import styles from "./WorkspaceSettingsButton.module.css";

interface WorkspaceSettingsButtonProps {
  workspaceName?: string;
  requestClose: () => void;
}

const WorkspaceSettingsButton: React.FC<WorkspaceSettingsButtonProps> = ({ workspaceName, requestClose }) => {
  const { t } = useTranslation();

  return (
    <div className={styles.infoContainer}>
      <div className={cn(styles.profilePicContainer, styles["profilePicContainer-without-profile-pic"])}>
        <div className={styles.firstLetter}>
          <LuSettings />
        </div>
      </div>
      <Button
        className={styles.nameContainer}
        href={workspaceName ? `/${workspaceName}/settings` : undefined}
        onClick={requestClose}
      >
        <span className={styles.title}>{t("workspaceSettingsMoreMenuButton")}</span>
      </Button>
    </div>
  );
};

export default WorkspaceSettingsButton;
