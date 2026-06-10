import React from "react";
import styles from "./NewProjectModal.module.css";
import Modal from "@/components/modal/modal/Modal";
import { closeNewProjectModal, selectNewProjectModalVisible, selectNewProjectModalWorkspace } from "@/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "@/locals/useTranslation";
import NewProjectForm from "@/components/form/newProjectForm/NewProjectForm";

interface NewProjectModalProps {

}

const NewProjectModal: React.FC<NewProjectModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const visible = useTypedSelector(selectNewProjectModalVisible);
  const workspace = useTypedSelector(selectNewProjectModalWorkspace);

  const close = () => {
    dispatch(closeNewProjectModal());
  };

  return (
    <Modal
      visible={visible}
      bodyClass={styles.container}
      width={"xxlarge"}
      height={"height-full"}
      containerClassName={styles.modalContainer}
    >
      {workspace && <NewProjectForm workspace={workspace} onClose={close} />}
    </Modal>

  );
};

export default NewProjectModal;