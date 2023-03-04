import NewWorkspaceForm from "@/components/form/newWorkspaceForm/NewWorkspaceForm";
import useWindowSize from "@/hooks/useWindowSize";
import { closeNewWorkspaceModal, selectNewWorkspaceModalVisible } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import Modal from "../modal/Modal";
import styles from "./NewWorkspaceModal.module.css";

interface NewWorkspaceModalProps {}

const NewWorkspaceModal: React.FC<NewWorkspaceModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const { isMobile } = useWindowSize();
  const visible = useTypedSelector(selectNewWorkspaceModalVisible);

  const close = () => {
    dispatch(closeNewWorkspaceModal());
  };

  return (
    <Modal
      visible={visible}
      title={t("newWorkspaceModalTitle")}
      bodyClass={styles.container}
      width={isMobile ? "fullscreen" : "large"}
      hasTitleCloseButton={true}
      requestClose={close}
    >
      <NewWorkspaceForm close={close} />
    </Modal>
  );
};

export default NewWorkspaceModal;
