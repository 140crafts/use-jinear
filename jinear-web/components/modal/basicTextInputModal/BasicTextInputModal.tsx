import Button, { ButtonVariants } from "@/components/button";
import {
  closeBasicTextInputModal,
  selectBasicTextInputModalInfoInitialText,
  selectBasicTextInputModalInfoOnSubmit,
  selectBasicTextInputModalInfoText,
  selectBasicTextInputModalTitle,
  selectBasicTextInputModalVisible,
} from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React, { ChangeEvent, useEffect, useState } from "react";
import Modal from "../modal/Modal";
import styles from "./BasicTextInputModal.module.css";

interface BasicTextInputModalProps {}

const BasicTextInputModal: React.FC<BasicTextInputModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const visible = useTypedSelector(selectBasicTextInputModalVisible);
  const title = useTypedSelector(selectBasicTextInputModalTitle);
  const infoText = useTypedSelector(selectBasicTextInputModalInfoText);
  const initialText = useTypedSelector(selectBasicTextInputModalInfoInitialText);
  const onSubmit = useTypedSelector(selectBasicTextInputModalInfoOnSubmit);

  const [text, setText] = useState<string>(initialText || "");

  useEffect(() => {
    if (initialText) {
      setText(initialText);
    }
  }, [initialText]);

  const onTextChange = (e: ChangeEvent<HTMLInputElement>) => {
    setText(e.target.value);
  };

  const close = () => {
    dispatch(closeBasicTextInputModal());
  };

  const submit = () => {
    onSubmit?.(text || "");
  };

  return (
    <Modal visible={visible} title={title} bodyClass={styles.container} requestClose={close} width={"default"}>
      <div>{infoText}</div>
      <input type="text" onChange={onTextChange} value={text} />
      <div className={styles.actionBarContainer}>
        <Button onClick={close}>{t("basicTextInputModalCancelLabel")}</Button>
        <Button variant={ButtonVariants.contrast} onClick={submit}>
          {t("basicTextInputModalSubmitLabel")}
        </Button>
      </div>
    </Modal>
  );
};

export default BasicTextInputModal;
