import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import SectionTitle from "@/components/sectionTitle/SectionTitle";
import { WorkspaceDto } from "@/model/be/jinear-core";
import { popWorkspaceMemberInviteModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./WorkspaceMembersScreenHeader.module.css";

interface WorkspaceMembersScreenHeaderProps {
  workspace: WorkspaceDto;
  isWorkspaceAdminOrOwner: boolean;
}

const WorkspaceMembersScreenHeader: React.FC<WorkspaceMembersScreenHeaderProps> = ({ workspace, isWorkspaceAdminOrOwner }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const popInviteModal = () => {
    dispatch(popWorkspaceMemberInviteModal({ visible: true, workspaceId: workspace.workspaceId }));
  };
  return (
    <div className={styles.container}>
      <SectionTitle title={t("activeWorkspaceMemberListTitle")} description={t("activeWorkspaceMemberListText")} />
      <div className={styles.actionBar}>
        {isWorkspaceAdminOrOwner && (
          <Button variant={ButtonVariants.contrast} heightVariant={ButtonHeight.short} onClick={popInviteModal}>
            {t("workspaceMemberScreenInviteMember")}
          </Button>
        )}
      </div>
    </div>
  );
};

export default WorkspaceMembersScreenHeader;
