import Breadcrumb from "@/components/breadcrumb/Breadcrumb";
import BreadcrumbLink from "@/components/breadcrumb/BreadcrumbLink";
import Button, { ButtonVariants } from "@/components/button";
import { popWorkspaceMemberInviteModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./WorkspaceMembersScreenHeader.module.css";

interface WorkspaceMembersScreenHeaderProps {
  workspaceUsername: string;
}

const WorkspaceMembersScreenHeader: React.FC<WorkspaceMembersScreenHeaderProps> = ({ workspaceUsername }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const popInviteModal = () => {
    dispatch(popWorkspaceMemberInviteModal());
  };
  return (
    <div className={styles.container}>
      <Breadcrumb>
        <BreadcrumbLink label={workspaceUsername} url={`/${workspaceUsername}`} />
        <BreadcrumbLink label={t("workspaceMemberScreenBreadcrumbTitle")} url={`/${workspaceUsername}/members`} />
      </Breadcrumb>
      <div className="spacer-h-4" />
      <div className={styles.actionBar}>
        <Button variant={ButtonVariants.contrast} onClick={popInviteModal}>
          {t("workspaceMemberScreenInviteMember")}
        </Button>
      </div>
    </div>
  );
};

export default WorkspaceMembersScreenHeader;
