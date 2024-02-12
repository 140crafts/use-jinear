import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { CalendarEventDto } from "@/model/be/jinear-core";
import { useUpdateTitleAndDescriptionMutation } from "@/store/api/calendarEventApi";
import { closeBasicTextInputModal, popBasicTextInputModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import { CircularProgress } from "@mui/material";
import useTranslation from "locales/useTranslation";
import React, { useEffect, useState } from "react";
import { LuPencil } from "react-icons/lu";
import styles from "./EventTitle.module.css";

interface EventTitleProps {
  calendarEvent: CalendarEventDto;
}

const EventTitle: React.FC<EventTitleProps> = ({ calendarEvent }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const [updateTitleAndDescription, { isSuccess: isUpdateSuccess, isLoading: isUpdateLoading }] =
    useUpdateTitleAndDescriptionMutation();
  const [taskTitle, setTaskTitle] = useState(calendarEvent.title);

  useEffect(() => {
    setTaskTitle(calendarEvent.title);
  }, [calendarEvent.title]);

  const changeTitle = (title: string) => {
    dispatch(closeBasicTextInputModal());
    if (title && calendarEvent.externalCalendarSourceDto) {
      const req = {
        calendarId: calendarEvent.calendarId,
        calendarSourceId: calendarEvent.externalCalendarSourceDto.externalCalendarSourceId,
        calendarEventId: calendarEvent.calendarEventId,
        title,
      };
      updateTitleAndDescription(req);
      setTaskTitle(title);
    }
  };

  const popTitleChangeModal = () => {
    dispatch(
      popBasicTextInputModal({
        visible: true,
        title: t("calendarEventTitleChangeModalTitle"),
        infoText: t("calendarEventTitleChangeModalInfoText"),
        initialText: calendarEvent.title,
        onSubmit: changeTitle,
      })
    );
  };

  return (
    <div className={styles.container}>
      <h1>
        <b>{taskTitle}</b>
      </h1>
      {isUpdateLoading && <CircularProgress size={16} />}
      {isUpdateLoading && <span>{t("calendarTitleSaving")}</span>}
      {!isUpdateLoading && (
        <Button
          disabled={isUpdateLoading}
          heightVariant={ButtonHeight.short}
          variant={ButtonVariants.filled}
          className={styles.editTitleButton}
          onClick={popTitleChangeModal}
        >
          <LuPencil />
        </Button>
      )}
    </div>
  );
};

export default EventTitle;
