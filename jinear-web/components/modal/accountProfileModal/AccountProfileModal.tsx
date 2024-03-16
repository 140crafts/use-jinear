import AccountDeleteButton from "@/components/profileScreen/accountDeleteButton/AccountDeleteButton";
import CommunicationPreferences from "@/components/profileScreen/communicationPreferences/CommunicationPreferences";
import PersonalInfoTab from "@/components/profileScreen/personalInfoTab/PersonalInfoTab";
import useWindowSize from "@/hooks/useWindowSize";
import { closeAccountProfileModal, selectAccountProfileModalVisible } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import Modal from "../modal/Modal";

interface AccountProfileModalProps {}

const AccountProfileModal: React.FC<AccountProfileModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const visible = useTypedSelector(selectAccountProfileModalVisible);
  const { isMobile } = useWindowSize();

  const close = () => {
    dispatch(closeAccountProfileModal());
  };

  return (
    <Modal
      visible={visible}
      width={isMobile ? "fullscreen" : "medium-fixed"}
      title={t("accountProfileModalTitle")}
      hasTitleCloseButton={true}
      requestClose={close}
    >
      <PersonalInfoTab />
      <CommunicationPreferences title={t("communicationPrefrencesTitle")} />
      <div className="spacer-h-4" />
      <AccountDeleteButton />
      <div className="spacer-h-2" />
    </Modal>
  );
};

export default AccountProfileModal;
