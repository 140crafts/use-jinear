import WorkspaceMemberInvitationForm from "@/components/form/workspaceMemberInvitationForm/WorkspaceMemberInvitationForm";
import useWindowSize from "@/hooks/useWindowSize";
import { selectCurrentAccountsPreferredWorkspaceId } from "@/store/slice/accountSlice";
import { closeWorkspaceMemberInviteModal, selectWorkspaceMemberInviteModalVisible } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import Modal from "../modal/Modal";

interface WorkspaceInviteMemberModalProps {}

const WorkspaceInviteMemberModal: React.FC<WorkspaceInviteMemberModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const visible = useTypedSelector(selectWorkspaceMemberInviteModalVisible);
  const preferredWorkspaceId = useTypedSelector(selectCurrentAccountsPreferredWorkspaceId);
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
      {preferredWorkspaceId && <WorkspaceMemberInvitationForm workspaceId={preferredWorkspaceId} onInviteSuccess={close} />}
    </Modal>
  );
};

export default WorkspaceInviteMemberModal;
