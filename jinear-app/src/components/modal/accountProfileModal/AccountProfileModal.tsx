import AccountDeleteButton from "@/components/profile-screen/accountDeleteButton/AccountDeleteButton";
import CommunicationPreferences from "@/components/profile-screen/communicationPreferences/CommunicationPreferences";
import McpSettingsButton from "@/components/profile-screen/mcpSettingsButton/McpSettingsButton";
import PersonalInfoTab from "@/components/profile-screen/personalInfoTab/PersonalInfoTab";
import useWindowSize from "@/hooks/useWindowSize";
import { closeAccountProfileModal, selectAccountProfileModalVisible } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store";
import useTranslation from "@/locales/useTranslation";
import React from "react";
import Modal from "../modal/Modal";
import styles from "./AccountProfileModal.module.css";

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
      <div className={styles.content}>
        <PersonalInfoTab />
        <CommunicationPreferences title={t("communicationPrefrencesTitle")} />
        <McpSettingsButton />
        <AccountDeleteButton />
      </div>
    </Modal>
  );
};

export default AccountProfileModal;
