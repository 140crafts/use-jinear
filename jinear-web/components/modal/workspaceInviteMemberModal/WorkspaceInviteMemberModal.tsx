import WorkspaceMemberInvitationForm from "@/components/form/workspaceMemberInvitationForm/WorkspaceMemberInvitationForm";
import useWindowSize from "@/hooks/useWindowSize";
import {
  closeWorkspaceMemberInviteModal,
  selectWorkspaceMemberInviteModalVisible,
  selectWorkspaceMemberInviteModalWorkspaceId,
} from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import Modal from "../modal/Modal";

interface WorkspaceInviteMemberModalProps {}

const WorkspaceInviteMemberModal: React.FC<WorkspaceInviteMemberModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const visible = useTypedSelector(selectWorkspaceMemberInviteModalVisible);
  const workspaceId = useTypedSelector(selectWorkspaceMemberInviteModalWorkspaceId);
  const { isMobile } = useWindowSize();

  const close = () => {
    dispatch(closeWorkspaceMemberInviteModal());
  };

  return (
    <Modal
      visible={visible}
      width={isMobile ? "fullscreen" : "medium-fixed"}
      title={t("workspaceMemberInviteModalTitle")}
      hasTitleCloseButton={true}
      requestClose={close}
    >
      {workspaceId && <WorkspaceMemberInvitationForm workspaceId={workspaceId} onInviteSuccess={close} />}
    </Modal>
  );
};

export default WorkspaceInviteMemberModal;
