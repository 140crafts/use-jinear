import Button from "@/components/button";
import CircularLoading from "@/components/circularLoading/CircularLoading";
import useWindowSize from "@/hooks/useWindowSize";
import { root } from "@/store/api/api";
import { useRefreshShareableKeyMutation, useRetrieveShareableKeyQuery } from "@/store/api/calendarEventApi";
import {
  closeCalendarShareEventsModal,
  selectCalendarShareEventsModalVisible,
  selectCalendarShareEventsModalWorkspaceId,
} from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import { copyTextToClipboard } from "@/utils/clipboard";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoRefresh } from "react-icons/io5";
import Modal from "../modal/Modal";
import styles from "./ShareWorkspaceEventsModal.module.css";

interface ShareWorkspaceEventsModalProps {}

const ShareWorkspaceEventsModal: React.FC<ShareWorkspaceEventsModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const visible = useTypedSelector(selectCalendarShareEventsModalVisible);
  const workspaceId = useTypedSelector(selectCalendarShareEventsModalWorkspaceId);
  const { isMobile } = useWindowSize();
  const [refreshShareableKey, { isLoading: isRefreshLoading }] = useRefreshShareableKeyMutation();
  const { data: calendarShareableKeyResponse, isLoading: retrieveShareableKeyLoading } = useRetrieveShareableKeyQuery(
    { workspaceId: workspaceId || "" },
    { skip: !workspaceId }
  );
  const link = `${root}v1/calendar/event/exports/${calendarShareableKeyResponse?.data.shareableKey}`;

  const refreshLink = () => {
    if (workspaceId) {
      refreshShareableKey({ workspaceId });
    }
  };

  const close = () => {
    dispatch(closeCalendarShareEventsModal());
  };

  const copyLinkToClipboard = () => {
    copyTextToClipboard(link);
  };

  return (
    <Modal
      visible={visible}
      width={isMobile ? "fullscreen" : "medium-fixed"}
      title={t("calendarEventsShareableKeyModalTitle")}
      hasTitleCloseButton={true}
      requestClose={close}
    >
      <span className={styles.infoText}>{t("calendarEventsShareableKeyModalText")}</span>

      <div className="spacer-h-2" />
      {(retrieveShareableKeyLoading || isRefreshLoading) && <CircularLoading />}
      {calendarShareableKeyResponse && !retrieveShareableKeyLoading && !isRefreshLoading && (
        <>
          <div className={styles.inputContainer}>
            <input type="text" disabled={true} value={link} className={styles.link} />
            <Button onClick={refreshLink}>
              <IoRefresh />
            </Button>
          </div>
          <div className="spacer-h-1" />
          <Button onClick={copyLinkToClipboard}>{t("calendarEventsShareableKeyModalCopy")}</Button>
        </>
      )}

      <div className="spacer-h-2" />

      <span className={styles.subInfoText}>{t("calendarEventsShareableKeyModalSubText")}</span>

      <div className="spacer-h-1" />
    </Modal>
  );
};

export default ShareWorkspaceEventsModal;
