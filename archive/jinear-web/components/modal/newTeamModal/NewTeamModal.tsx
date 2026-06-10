import NewTeamForm from "@/components/form/newTeamForm/NewTeamForm";
import useWindowSize from "@/hooks/useWindowSize";
import { closeNewTeamModal, selectNewTeamModalVisible, selectNewTeamModalWorkspace } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import Modal from "../modal/Modal";
import styles from "./NewTeamModal.module.css";

interface NewTeamModalProps {}

const NewTeamModal: React.FC<NewTeamModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const { isMobile } = useWindowSize();
  const visible = useTypedSelector(selectNewTeamModalVisible);
  const workspace = useTypedSelector(selectNewTeamModalWorkspace);

  const close = () => {
    dispatch(closeNewTeamModal());
  };
  return (
    <Modal
      visible={visible}
      title={t("newTeamModalTitle")}
      bodyClass={styles.container}
      width={isMobile ? "fullscreen" : "large"}
      hasTitleCloseButton={true}
      requestClose={close}
    >
      {workspace && <NewTeamForm close={close} workspace={workspace} />}
    </Modal>
  );
};

export default NewTeamModal;
