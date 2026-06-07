import AddMemberToTeamForm from "@/components/form/addMemberToTeamForm/AddMemberToTeamForm";
import useWindowSize from "@/hooks/useWindowSize";
import {
  closeAddMemberToTeamModal,
  selectAddMemberToTeamModalTeam,
  selectAddMemberToTeamModalVisible,
  selectAddMemberToTeamModalWorkspace,
} from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import Modal from "../modal/Modal";

interface AddMemberToTeamModalProps {}

const AddMemberToTeamModal: React.FC<AddMemberToTeamModalProps> = ({}) => {
  const { t } = useTranslation();
  const { isMobile } = useWindowSize();
  const dispatch = useAppDispatch();
  const visible = useTypedSelector(selectAddMemberToTeamModalVisible);
  const workspace = useTypedSelector(selectAddMemberToTeamModalWorkspace);
  const team = useTypedSelector(selectAddMemberToTeamModalTeam);

  const close = () => {
    dispatch(closeAddMemberToTeamModal());
  };

  return (
    <Modal
      visible={visible}
      width={isMobile ? "fullscreen" : "medium-fixed"}
      title={t("addMemberToTeamModalTitle")}
      hasTitleCloseButton={true}
      requestClose={close}
    >
      {workspace && team && (
        <AddMemberToTeamForm
          workspaceId={workspace.workspaceId}
          workspaceName={workspace.username}
          teamId={team.teamId}
          onAddSuccess={close}
          requestClose={close}
        />
      )}
    </Modal>
  );
};

export default AddMemberToTeamModal;
