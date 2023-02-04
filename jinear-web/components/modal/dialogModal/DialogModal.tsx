import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import {
  closeDialogModal,
  selectDialogModalCloseButtonLabel,
  selectDialogModalConfirmButtonLabel,
  selectDialogModalContent,
  selectDialogModalHtmlContent,
  selectDialogModalOnClose,
  selectDialogModalOnConfirm,
  selectDialogModalTitle,
  selectDialogModalVisible,
} from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import Modal from "../modal/Modal";
import styles from "./DialogModal.module.css";

interface DialogModalProps {}

const DialogModal: React.FC<DialogModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const visible = useTypedSelector(selectDialogModalVisible);
  const title = useTypedSelector(selectDialogModalTitle);
  const content = useTypedSelector(selectDialogModalContent);
  const htmlContent = useTypedSelector(selectDialogModalHtmlContent);
  const closeButtonLabel =
    useTypedSelector(selectDialogModalCloseButtonLabel) ||
    t("dialogModalGenericCloseLabel");
  const confirmButtonLabel = useTypedSelector(
    selectDialogModalConfirmButtonLabel
  );
  const onConfirm = useTypedSelector(selectDialogModalOnConfirm);
  const onClose = useTypedSelector(selectDialogModalOnClose);

  const close = () => {
    dispatch(closeDialogModal());
  };

  return (
    <Modal
      visible={visible}
      title={title}
      bodyClass={styles.container}
      requestClose={onClose ? onClose : close}
      width={"default"}
    >
      {htmlContent ? (
        <div dangerouslySetInnerHTML={{ __html: htmlContent }}></div>
      ) : (
        <div>{content}</div>
      )}

      <div className={styles.actionBar}>
        {onConfirm && (
          <Button
            heightVariant={ButtonHeight.mid}
            variant={ButtonVariants.filled}
            onClick={onConfirm}
          >
            {confirmButtonLabel}
          </Button>
        )}
        <Button
          heightVariant={ButtonHeight.mid}
          variant={ButtonVariants.contrast}
          onClick={onClose ? onClose : close}
        >
          {closeButtonLabel}
        </Button>
      </div>
    </Modal>
  );
};

export default DialogModal;
