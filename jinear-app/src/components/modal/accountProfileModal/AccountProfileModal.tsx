import AccountDeleteButton from "@/components/profile-screen/accountDeleteButton/AccountDeleteButton";
import CommunicationPreferences from "@/components/profile-screen/communicationPreferences/CommunicationPreferences";
import PersonalInfoTab from "@/components/profile-screen/personalInfoTab/PersonalInfoTab";
import useWindowSize from "@/hooks/useWindowSize";
import { closeAccountProfileModal, selectAccountProfileModalVisible } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store";
import useTranslation from "@/locales/useTranslation";
import React from "react";
import Modal from "../modal/Modal";
import McpConnectionsSection from "@/components/profile-screen/mcpConnectionsSection/McpConnectionsSection.tsx";
import HorizontalLine from "@/components/line/horizontalLine/HorizontalLine.tsx";

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
      <div>
        <div className="spacer-h-1" />
        <HorizontalLine />
        <div className="spacer-h-1" />
        <AccountDeleteButton />
        <div className="spacer-h-1" />
        <HorizontalLine />
        <div className="spacer-h-1" />
        <McpConnectionsSection title={t("mcpConnectionsTitle")}/>
        <div className="spacer-h-2" />
      </div>

    </Modal>
  );
};

export default AccountProfileModal;
