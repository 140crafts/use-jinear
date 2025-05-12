import WorkspaceInfoList from "@/components/workspaceInfoList/WorkspaceInfoList";
import useWindowSize from "@/hooks/useWindowSize";
import { closeWorkspaceSwitchModal, selectWorkspaceSwitchModalVisible } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import Modal from "../modal/Modal";
import styles from "./WorkspaceSwitchModal.module.css";

interface WorkspaceSwitchModalProps {}

const WorkspaceSwitchModal: React.FC<WorkspaceSwitchModalProps> = ({}) => {
  const { t } = useTranslation();
  const { isMobile } = useWindowSize();
  const dispatch = useAppDispatch();
  const visible = useTypedSelector(selectWorkspaceSwitchModalVisible);

  const close = () => {
    dispatch(closeWorkspaceSwitchModal());
  };

  return (
    <Modal
      visible={visible}
      width={isMobile ? "fullscreen" : "medium-fixed"}
      title={t("workspaceSwitchModalTitle")}
      hasTitleCloseButton={true}
      requestClose={close}
      bodyClass={styles.contentContainer}
    >
      <WorkspaceInfoList onWorkspaceChangeComplete={close} />
    </Modal>
  );
};

export default WorkspaceSwitchModal;
