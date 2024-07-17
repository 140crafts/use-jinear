import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import useWindowSize from "@/hooks/useWindowSize";
import {
  closeCalendarExternalEventViewModal,
  selectCalendarExternalEventViewModalCalendarEvent,
  selectCalendarExternalEventViewModalVisible
} from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import { LuExternalLink } from "react-icons/lu";
import Modal from "../modal/Modal";
import styles from "./CalendarExternalEventViewModal.module.scss";
import EventSource from "@/components/modal/calendarExternalEventViewModal/eventSource/EventSource";
import EventBody from "./eventBody/EventBody";
import ExistingEventDateButtons from "@/components/existingEventDateButtons/ExistingEventDateButtons";

interface CalendarExternalEventViewModalProps {
}

const CalendarExternalEventViewModal: React.FC<CalendarExternalEventViewModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const { isMobile } = useWindowSize();

  const visible = useTypedSelector(selectCalendarExternalEventViewModalVisible);
  const calendarEvent = useTypedSelector(selectCalendarExternalEventViewModalCalendarEvent);

  const close = () => {
    dispatch(closeCalendarExternalEventViewModal());
  };

  return (
    <Modal
      visible={visible}
      width={isMobile ? "fullscreen" : "large"}
      title={calendarEvent?.title}
      hasTitleCloseButton={true}
      requestClose={close}
      bodyClass={styles.modalBody}
      contentContainerClass={styles.modal}
      containerClassName={styles.modalContainer}
    >
      {calendarEvent && (
        <div className={styles.eventContentWrapper}>
          <div className={styles.eventDetailLayout}>
            <EventBody calendarEvent={calendarEvent} />
            <div className={styles.eventActionBar}>
              <EventSource calendarEvent={calendarEvent} />
              <ExistingEventDateButtons calendarEvent={calendarEvent} />
              {calendarEvent?.externalLink && (
                <Button
                  heightVariant={ButtonHeight.short}
                  variant={ButtonVariants.filled}
                  className={styles.goToSourceButton}
                  href={calendarEvent.externalLink}
                  target="_blank"
                >
                  <LuExternalLink />
                  <span>{t("calendarEventModalGoToSourceLink")}</span>
                </Button>
              )}
            </div>
          </div>

          <div className={styles.actionBar}>

          </div>
        </div>
      )}
    </Modal>
  );
};

export default CalendarExternalEventViewModal;
