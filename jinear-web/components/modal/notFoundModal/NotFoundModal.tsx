import Button, { ButtonVariants } from "@/components/button";
import {
  closeNotFoundModal,
  selectNotFoundModalImgAlt,
  selectNotFoundModalImgSrc,
  selectNotFoundModalLabel,
  selectNotFoundModalTitle,
  selectNotFoundModalVisible,
} from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import Modal from "../modal/Modal";
import styles from "./NotFoundModal.module.css";

interface NotFoundModalProps {}

const NotFoundModal: React.FC<NotFoundModalProps> = ({}) => {
  const visibile = useTypedSelector(selectNotFoundModalVisible);
  const title = useTypedSelector(selectNotFoundModalTitle);
  const label = useTypedSelector(selectNotFoundModalLabel);
  const imgSrc = useTypedSelector(selectNotFoundModalImgSrc);
  const imgAlt = useTypedSelector(selectNotFoundModalImgAlt);
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const close = () => {
    dispatch(closeNotFoundModal());
  };

  return (
    <Modal visible={visibile} title={title} contentContainerClass={styles.container}>
      {label && <span>{label}</span>}
      <div className="spacer-h-4" />
      <Button onClick={close} href={"/"} variant={ButtonVariants.contrast}>
        {t("notFoundModalReturnHomeButtonLabel")}
      </Button>
    </Modal>
  );
};

export default NotFoundModal;
