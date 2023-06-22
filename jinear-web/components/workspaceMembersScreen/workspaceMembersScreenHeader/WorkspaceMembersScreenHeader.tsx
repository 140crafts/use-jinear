import Breadcrumb from "@/components/breadcrumb/Breadcrumb";
import BreadcrumbLink from "@/components/breadcrumb/BreadcrumbLink";
import Button, { ButtonVariants } from "@/components/button";
import { WorkspaceDto } from "@/model/be/jinear-core";
import { popWorkspaceMemberInviteModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./WorkspaceMembersScreenHeader.module.css";

interface WorkspaceMembersScreenHeaderProps {
  workspace: WorkspaceDto;
}

const WorkspaceMembersScreenHeader: React.FC<WorkspaceMembersScreenHeaderProps> = ({ workspace }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const popInviteModal = () => {
    dispatch(popWorkspaceMemberInviteModal({ visible: true, workspaceId: workspace.workspaceId }));
  };
  return (
    <div className={styles.container}>
      <Breadcrumb>
        <BreadcrumbLink label={workspace.username} url={`/${workspace.username}`} />
        <BreadcrumbLink label={t("workspaceMemberScreenBreadcrumbTitle")} url={`/${workspace.username}/members`} />
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
