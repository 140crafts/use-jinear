import {
  closeChangeTaskWorkflowStatusModal,
  selectChangeTaskWorkflowStatusModalTask,
  selectChangeTaskWorkflowStatusModalVisible,
} from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import Modal from "../../modal/Modal";
import styles from "./WorkflowChangeStatusModal.module.css";
import TeamWorkflowStatusList from "./teamWorkflowStatusList/TeamWorkflowStatusList";

interface WorkflowChangeStatusModalProps {}

const WorkflowChangeStatusModal: React.FC<WorkflowChangeStatusModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const visible = useTypedSelector(selectChangeTaskWorkflowStatusModalVisible);
  const task = useTypedSelector(selectChangeTaskWorkflowStatusModalTask);
  const teamId = task?.teamId;

  const close = () => {
    dispatch(closeChangeTaskWorkflowStatusModal());
  };

  return (
    <Modal
      visible={visible}
      title={t("changeTaskWorkflowStatusModalTitle")}
      bodyClass={styles.container}
      hasTitleCloseButton={true}
      requestClose={close}
    >
      {teamId && task && <TeamWorkflowStatusList teamId={teamId} />}
    </Modal>
  );
};

export default WorkflowChangeStatusModal;
