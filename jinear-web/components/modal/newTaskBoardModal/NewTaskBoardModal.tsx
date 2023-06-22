import NewTaskBoardForm from "@/components/form/newTaskBoardForm/NewTaskBoardForm";
import {
  closeNewTaskBoardModal,
  selectNewTaskBoardModalTeam,
  selectNewTaskBoardModalVisible,
  selectNewTaskBoardModalWorkspace,
} from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import Modal from "../modal/Modal";
import styles from "./NewTaskBoardModal.module.css";

interface NewTaskBoardModalProps {}

const NewTaskBoardModal: React.FC<NewTaskBoardModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const visible = useTypedSelector(selectNewTaskBoardModalVisible);
  const workspace = useTypedSelector(selectNewTaskBoardModalWorkspace);
  const team = useTypedSelector(selectNewTaskBoardModalTeam);

  const close = () => {
    dispatch(closeNewTaskBoardModal());
  };

  return (
    <Modal visible={visible} title={t("newTaskBoardModalTitle")} bodyClass={styles.container} requestClose={close}>
      {workspace && team && <NewTaskBoardForm workspace={workspace} team={team} onClose={close} />}
    </Modal>
  );
};

export default NewTaskBoardModal;
