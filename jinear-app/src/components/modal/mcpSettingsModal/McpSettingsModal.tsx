import McpConnectionsSection from "@/components/profile-screen/mcpConnectionsSection/McpConnectionsSection";
import useWindowSize from "@/hooks/useWindowSize";
import { closeMcpSettingsModal, selectMcpSettingsModalVisible } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store";
import useTranslation from "@/locales/useTranslation";
import React from "react";
import Modal from "../modal/Modal";

interface McpSettingsModalProps {}

const McpSettingsModal: React.FC<McpSettingsModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const visible = useTypedSelector(selectMcpSettingsModalVisible);
  const { isMobile } = useWindowSize();

  const close = () => {
    dispatch(closeMcpSettingsModal());
  };

  return (
    <Modal
      visible={visible}
      width={isMobile ? "fullscreen" : "medium-fixed"}
      title={t("mcpConnectionsTitle")}
      hasTitleCloseButton={true}
      requestClose={close}
    >
      <McpConnectionsSection />
      <div className="spacer-h-2" />
    </Modal>
  );
};

export default McpSettingsModal;
