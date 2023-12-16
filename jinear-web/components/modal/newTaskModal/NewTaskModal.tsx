import NewTaskForm from "@/components/form/newTaskForm/NewTaskForm";
import useWindowSize from "@/hooks/useWindowSize";
import {
  closeNewTaskModal,
  selectNewTaskModalInitialAssignedDate,
  selectNewTaskModalInitialAssignedDateIsPrecise,
  selectNewTaskModalInitialDueDate,
  selectNewTaskModalInitialDueDateIsPrecise,
  selectNewTaskModalInitialRelatedFeedItemData,
  selectNewTaskModalSubTaskOf,
  selectNewTaskModalSubTaskOfLabel,
  selectNewTaskModalTeam,
  selectNewTaskModalVisible,
  selectNewTaskModalWorkspace,
} from "@/store/slice/modalSlice";
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
  const subTaskOf = useTypedSelector(selectNewTaskModalSubTaskOf);
  const subTaskOfLabel = useTypedSelector(selectNewTaskModalSubTaskOfLabel);
  const workspace = useTypedSelector(selectNewTaskModalWorkspace);
  const team = useTypedSelector(selectNewTaskModalTeam);
  const initialAssignedDate = useTypedSelector(selectNewTaskModalInitialAssignedDate);
  const initialAssignedDateIsPrecise = useTypedSelector(selectNewTaskModalInitialAssignedDateIsPrecise);
  const initialDueDate = useTypedSelector(selectNewTaskModalInitialDueDate);
  const initialDueDateIsPrecise = useTypedSelector(selectNewTaskModalInitialDueDateIsPrecise);
  const initialRelatedFeedItemData = useTypedSelector(selectNewTaskModalInitialRelatedFeedItemData);
  const workspaceId = workspace?.workspaceId;

  const { isMobile } = useWindowSize();

  const close = () => {
    dispatch(closeNewTaskModal());
  };

  return (
    <Modal
      visible={visible}
      // title={t("newTaskModalTitle")}
      bodyClass={styles.container}
      // width={isMobile ? "fullscreen" : "large"}
      width={"large"}
    >
      {workspaceId && team?.teamId && (
        <NewTaskForm
          workspace={workspace}
          initialTeam={team}
          subTaskOf={subTaskOf}
          subTaskOfLabel={subTaskOfLabel}
          initialAssignedDate={initialAssignedDate}
          initialAssignedDateIsPrecise={initialAssignedDateIsPrecise}
          initialDueDate={initialDueDate}
          initialDueDateIsPrecise={initialDueDateIsPrecise}
          initialRelatedFeedItemData={initialRelatedFeedItemData}
          onClose={close}
        />
      )}
    </Modal>
  );
};

export default NewTaskModal;
