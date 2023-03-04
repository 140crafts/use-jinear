import NewTaskForm from "@/components/form/newTaskForm/NewTaskForm";
import useWindowSize from "@/hooks/useWindowSize";
import { selectCurrentAccountsPreferredTeamId, selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { closeNewTaskModal, selectNewTaskModalVisible } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import Modal from "../modal/Modal";
import styles from "./NewTaskModal.module.css";

interface NewTaskModalProps {}

const NewTaskModal: React.FC<NewTaskModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const visible = useTypedSelector(selectNewTaskModalVisible);
  const preferredWorkspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);
  const workspaceId = preferredWorkspace?.workspaceId;
  const teamId = useTypedSelector(selectCurrentAccountsPreferredTeamId);

  const { isMobile } = useWindowSize();

  const close = () => {
    dispatch(closeNewTaskModal());
  };

  return (
    <Modal
      visible={visible}
      title={t("newTaskModalTitle")}
      bodyClass={styles.container}
      width={isMobile ? "fullscreen" : "large"}
    >
      {workspaceId && teamId && <NewTaskForm workspaceId={workspaceId} teamId={teamId} onClose={close} />}
    </Modal>
  );
};

export default NewTaskModal;
