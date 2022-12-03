import NewTaskForm from "@/components/form/newTaskForm/NewTaskForm";
import useWindowSize from "@/hooks/useWindowSize";
import { selectCurrentAccountsPreferredTeamId } from "@/store/slice/accountSlice";
import {
  selectNewTaskModalVisible,
  selectNewTaskModalWorkspaceId,
} from "@/store/slice/modalSlice";
import { useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import Modal from "../modal/Modal";
import styles from "./NewTaskModal.module.css";

interface NewTaskModalProps {}

const NewTaskModal: React.FC<NewTaskModalProps> = ({}) => {
  const { t } = useTranslation();
  const visible = useTypedSelector(selectNewTaskModalVisible);
  const workspaceId = useTypedSelector(selectNewTaskModalWorkspaceId);
  const teamId = useTypedSelector(selectCurrentAccountsPreferredTeamId);

  const { isMobile } = useWindowSize();
  return (
    <Modal
      visible={visible}
      title={t("newTaskModalTitle")}
      bodyClass={styles.container}
      width={isMobile ? "fullscreen" : "large"}
    >
      {workspaceId && teamId && (
        <NewTaskForm workspaceId={workspaceId} teamId={teamId} />
      )}
    </Modal>
  );
};

export default NewTaskModal;
