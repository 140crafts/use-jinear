import AddMemberToTeamForm from "@/components/form/addMemberToTeamForm/AddMemberToTeamForm";
import useWindowSize from "@/hooks/useWindowSize";
import { selectCurrentAccountsPreferredTeamId, selectCurrentAccountsPreferredWorkspaceId } from "@/store/slice/accountSlice";
import { closeAddMemberToTeamModal, selectAddMemberToTeamModalVisible } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import Modal from "../modal/Modal";

interface AddMemberToTeamModalProps {}

const AddMemberToTeamModal: React.FC<AddMemberToTeamModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const visible = useTypedSelector(selectAddMemberToTeamModalVisible);
  const preferredWorkspaceId = useTypedSelector(selectCurrentAccountsPreferredWorkspaceId);
  const preferredTeamId = useTypedSelector(selectCurrentAccountsPreferredTeamId);
  const { isMobile } = useWindowSize();

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
      {preferredWorkspaceId && preferredTeamId && (
        <AddMemberToTeamForm workspaceId={preferredWorkspaceId} teamId={preferredTeamId} onAddSuccess={close} />
      )}
    </Modal>
  );
};

export default AddMemberToTeamModal;
