import Button from "@/components/button";
import WorkspaceMembers from "@/components/sideMenu/workspaceMembers/WorkspaceMembers";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React from "react";
import { LuUsers } from "react-icons/lu";
import styles from "./WorkspaceMembersButton.module.css";

interface WorkspaceMembersButtonProps {
  workspaceName?: string;
  requestClose: () => void;
}

const WorkspaceMembersButton: React.FC<WorkspaceMembersButtonProps> = ({ workspaceName, requestClose }) => {
  const { t } = useTranslation();

  return (
    <div className={styles.infoContainer}>
      <div className={cn(styles.profilePicContainer, styles["profilePicContainer-without-profile-pic"])}>
        <div className={styles.firstLetter}>
          <LuUsers />
        </div>
      </div>
      <Button
        className={styles.nameContainer}
        href={workspaceName ? `/${workspaceName}/members` : undefined}
        onClick={requestClose}
      >
        <span className={styles.title}>{t("workspaceMembersMoreMenuButton")}</span>
        {workspaceName && <WorkspaceMembers workspaceName={workspaceName} />}
      </Button>
    </div>
  );
};

export default WorkspaceMembersButton;
