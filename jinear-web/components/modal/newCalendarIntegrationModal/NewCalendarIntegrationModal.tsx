import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import useWindowSize from "@/hooks/useWindowSize";
import { useRetrieveAttachCalendarRedirectInfoQuery } from "@/store/api/googleOAuthApi";
import {
  closeNewCalendarIntegrationModal,
  selectNewCalendarIntegrationModalVisible,
  selectNewCalendarIntegrationModalWorkspaceId,
} from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoLogoGoogle } from "react-icons/io5";
import Modal from "../modal/Modal";
import styles from "./NewCalendarIntegrationModal.module.css";

interface NewCalendarIntegrationModalProps {}

const NewCalendarIntegrationModal: React.FC<NewCalendarIntegrationModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const visible = useTypedSelector(selectNewCalendarIntegrationModalVisible);
  const workspaceId = useTypedSelector(selectNewCalendarIntegrationModalWorkspaceId);
  const { isMobile } = useWindowSize();
  const { data: retrieveAttachCalendarRedirectInfoResponse, isLoading } = useRetrieveAttachCalendarRedirectInfoQuery(
    { workspaceId: workspaceId || "" },
    { skip: !workspaceId }
  );
  const close = () => {
    dispatch(closeNewCalendarIntegrationModal());
  };

  return (
    <Modal
      visible={visible}
      width={isMobile ? "fullscreen" : "medium-fixed"}
      title={t("newCalendarIntegrationModalTitle")}
      hasTitleCloseButton={true}
      requestClose={close}
    >
      <span className={styles.infoText}>{t("newCalendarIntegrationModalLabel")}</span>
      <div className="spacer-h-2" />
      <span className={styles.subInfoText}>{t("newCalendarIntegrationModalLabelSubtext")}</span>
      <div className="spacer-h-4" />
      <Button
        loading={isLoading}
        disabled={isLoading}
        className={styles.addButton}
        variant={ButtonVariants.contrast}
        heightVariant={ButtonHeight.short}
        href={retrieveAttachCalendarRedirectInfoResponse?.redirectUrl}
        target="_blank"
      >
        <IoLogoGoogle />
        {t("newCalendarIntegrationModalAddGoogleCalendarLabel")}
      </Button>
      <div className="spacer-h-1" />
    </Modal>
  );
};

export default NewCalendarIntegrationModal;
