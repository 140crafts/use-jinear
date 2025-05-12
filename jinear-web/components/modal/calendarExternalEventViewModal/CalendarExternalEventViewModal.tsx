import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import useWindowSize from "@/hooks/useWindowSize";
import {
  closeCalendarExternalEventViewModal, closeDialogModal, popDialogModal,
  selectCalendarExternalEventViewModalCalendarEvent,
  selectCalendarExternalEventViewModalVisible
} from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import { LuExternalLink, LuTrash2 } from "react-icons/lu";
import Modal from "../modal/Modal";
import styles from "./CalendarExternalEventViewModal.module.scss";
import EventSource from "@/components/modal/calendarExternalEventViewModal/eventSource/EventSource";
import EventBody from "./eventBody/EventBody";
import ExistingEventDateButtons from "@/components/existingEventDateButtons/ExistingEventDateButtons";
import { useDeleteCalendarEventMutation } from "@/api/calendarEventApi";
import Logger from "@/utils/logger";

interface CalendarExternalEventViewModalProps {
}

const logger = Logger("CalendarExternalEventViewModal");

const CalendarExternalEventViewModal: React.FC<CalendarExternalEventViewModalProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const visible = useTypedSelector(selectCalendarExternalEventViewModalVisible);
  const calendarEvent = useTypedSelector(selectCalendarExternalEventViewModalCalendarEvent);
  const [deleteCalendarEvent] = useDeleteCalendarEventMutation();
  const readOnly = calendarEvent?.externalCalendarSourceDto?.readOnly;

  logger.log({ calendarEvent });

  const close = () => {
    dispatch(closeCalendarExternalEventViewModal());
  };

  const onDeleteEvent = () => {
    dispatch(
      popDialogModal({
        visible: true,
        title: t("deleteCalendarEventAreYouSureTitle"),
        content: t("deleteCalendarEventAreYouSureText"),
        confirmButtonLabel: t("deleteCalendarEventAreYouSureConfirmLabel"),
        onConfirm: deleteEvent
      })
    );
  };

  const deleteEvent = () => {
    if (calendarEvent && calendarEvent.externalCalendarSourceDto) {
      deleteCalendarEvent({
        calendarId: calendarEvent.calendarId,
        sourceId: calendarEvent.externalCalendarSourceDto.externalCalendarSourceId,
        eventId: calendarEvent.calendarEventId
      });
      dispatch(closeDialogModal());
      close();
    }
  };

  return (
    <Modal
      visible={visible}
      title={calendarEvent?.title ?? t("calendarTitleNotProvided")}
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

              {calendarEvent.externalCalendarSourceDto && !readOnly &&
                <Button
                  heightVariant={ButtonHeight.short}
                  variant={ButtonVariants.filled}
                  className={styles.actionButton}
                  onClick={onDeleteEvent}>
                  <LuTrash2 />
                  <span>{t("calendarEventModalDeleteEvent")}</span>
                </Button>
              }

              {calendarEvent?.externalLink && (
                <Button
                  heightVariant={ButtonHeight.short}
                  variant={ButtonVariants.filled}
                  className={styles.actionButton}
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
