import React from "react";
import styles from "./PasswordChangeModal.module.css";
import {
  closePasswordChangeModal,
  selectPasswordChangeModalForced,
  selectPasswordChangeModalVisible
} from "@/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import Modal from "@/components/modal/modal/Modal";
import useTranslation from "@/locals/useTranslation";
import ChangePasswordForm from "@/components/form/changePasswordForm/ChangePasswordForm";

interface PasswordChangeModalProps {

}

const PasswordChangeModal: React.FC<PasswordChangeModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const visible = useTypedSelector(selectPasswordChangeModalVisible);
  const forced = useTypedSelector(selectPasswordChangeModalForced);

  const close = () => {
    dispatch(closePasswordChangeModal());
  };

  return (
    <Modal
      visible={visible}
      title={t(forced ? "passwordChangeModalTitleForced" : "passwordChangeModalTitle")}
      hasTitleCloseButton={!forced}
      requestClose={forced ? undefined : close}
      bodyClass={styles.contentContainer}
    >
      <ChangePasswordForm forced={forced} onSuccess={close} />
    </Modal>
  );
};

export default PasswordChangeModal;