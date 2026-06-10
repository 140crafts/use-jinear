import useWindowSize from "@/hooks/useWindowSize";
import { selectCurrentAccountsWorkspaces } from "@/store/slice/accountSlice";
import { closeWorkspacePickerModal, selectWorkspacePickerModalVisible } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import { CircularProgress } from "@mui/material";
import useTranslation from "locales/useTranslation";
import React from "react";
import Modal from "../modal/Modal";
import styles from "./WorkspacePickerModal.module.css";
import BasicWorkspaceButton from "./basicWorkspaceButton/BasicWorkspaceButton";

interface WorkspacePickerModalProps {}

const WorkspacePickerModal: React.FC<WorkspacePickerModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const { isMobile } = useWindowSize();

  const visible = useTypedSelector(selectWorkspacePickerModalVisible);
  const workspaces = useTypedSelector(selectCurrentAccountsWorkspaces);

  const close = () => {
    dispatch(closeWorkspacePickerModal());
  };

  return (
    <Modal
      visible={visible}
      title={t("workspacePickerModalTitle")}
      bodyClass={styles.container}
      width={isMobile ? "fullscreen" : "large"}
      hasTitleCloseButton={true}
      requestClose={close}
    >
      {(!workspaces || workspaces?.length == 0) && (
        <div className={styles.loadingContainer}>
          <CircularProgress size={17} />
        </div>
      )}
      {workspaces && <div className={styles.title}>{t("workspacePickerModalPickAWorkspace")}</div>}
      {workspaces?.map((workspace) => (
        <BasicWorkspaceButton key={`basic-workspace-button-${workspace.workspaceId}`} workspace={workspace} close={close} />
      ))}
    </Modal>
  );
};

export default WorkspacePickerModal;
