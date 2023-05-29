import NewTaskListForm from "@/components/form/newTaskListForm/NewTaskListForm";
import { selectCurrentAccountsPreferredTeam, selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { closeNewTaskListModal, selectNewTaskListModalVisible } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import Modal from "../modal/Modal";
import styles from "./NewTaskListModal.module.css";

interface NewTaskListModalProps {}

const NewTaskListModal: React.FC<NewTaskListModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const visible = useTypedSelector(selectNewTaskListModalVisible);
  const workspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);
  const team = useTypedSelector(selectCurrentAccountsPreferredTeam);
  const close = () => {
    dispatch(closeNewTaskListModal());
  };

  return (
    <Modal visible={visible} title={t("newTaskListModalTitle")} bodyClass={styles.container} requestClose={close}>
      {workspace && team && <NewTaskListForm workspace={workspace} team={team} onClose={close} />}
    </Modal>
  );
};

export default NewTaskListModal;
