import {
  closeNewMailIntegrationModal,
  selectNewMailIntegrationModalVisible,
  selectNewMailIntegrationModalWorkspaceId,
} from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";

import Button, { ButtonVariants } from "@/components/button";
import useWindowSize from "@/hooks/useWindowSize";
import { useRetrieveAttachMailRedirectInfoQuery } from "@/store/api/googleOAuthApi";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoLogoGoogle } from "react-icons/io5";
import Modal from "../modal/Modal";
import styles from "./NewMailIntegrationModal.module.css";

interface NewMailIntegrationModalProps {}

const NewMailIntegrationModal: React.FC<NewMailIntegrationModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const visible = useTypedSelector(selectNewMailIntegrationModalVisible);
  const workspaceId = useTypedSelector(selectNewMailIntegrationModalWorkspaceId);
  const { isMobile } = useWindowSize();
  const { data: retrieveAttachMailRedirectInfoResponse, isLoading } = useRetrieveAttachMailRedirectInfoQuery(
    { workspaceId: workspaceId || "" },
    { skip: !workspaceId }
  );
  const close = () => {
    dispatch(closeNewMailIntegrationModal());
  };

  return (
    <Modal
      visible={visible}
      width={isMobile ? "fullscreen" : "medium-fixed"}
      title={t("newMailIntegrationModalTitle")}
      hasTitleCloseButton={true}
      requestClose={close}
    >
      <span className={styles.infoText}>{t("newMailIntegrationModalLabel")}</span>
      <div className="spacer-h-2" />
      <span className={styles.subInfoText}>{t("newMailIntegrationModalLabelSubtext")}</span>
      <div className="spacer-h-4" />
      <Button
        loading={isLoading}
        disabled={isLoading}
        className={styles.addButton}
        variant={ButtonVariants.contrast}
        href={retrieveAttachMailRedirectInfoResponse?.redirectUrl}
      >
        <IoLogoGoogle />
        {t("newMailIntegrationModalAddGmailLabel")}
      </Button>
      <div className="spacer-h-1" />
    </Modal>
  );
};

export default NewMailIntegrationModal;
