import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import Tiptap, { ITiptapRef } from "@/components/tiptap/Tiptap";
import { useToggle } from "@/hooks/useToggle";
import { CalendarEventDto } from "@/model/be/jinear-core";
import { useUpdateTitleAndDescriptionMutation } from "@/store/api/calendarEventApi";
import { CircularProgress } from "@mui/material";
import useTranslation from "locales/useTranslation";
import React, { useRef, useState } from "react";
import { LuPencil } from "react-icons/lu";
import styles from "./EventDescription.module.css";

interface EventDescriptionProps {
  calendarEvent: CalendarEventDto;
}

const EventDescription: React.FC<EventDescriptionProps> = ({ calendarEvent }) => {
  const { t } = useTranslation();
  const [readOnly, toggleReadOnly] = useToggle(true);
  const [initialValue, setInitialValue] = useState(calendarEvent.description?.value);
  const [updateTitleAndDescription, { isSuccess: isUpdateSuccess, isLoading: isUpdateLoading }] =
    useUpdateTitleAndDescriptionMutation();
  const tiptapRef = useRef<ITiptapRef>(null);
  const calendarReadOnly = calendarEvent?.externalCalendarSourceDto?.readOnly;

  const save = () => {
    const input: HTMLInputElement | null = document.getElementById(
      `${calendarEvent.description?.richTextId}`
    ) as HTMLInputElement;
    if (input && calendarEvent.externalCalendarSourceDto) {
      const value = input?.value || "";
      const req = {
        calendarId: calendarEvent.calendarId,
        calendarSourceId: calendarEvent.externalCalendarSourceDto.externalCalendarSourceId,
        calendarEventId: calendarEvent.calendarEventId,
        description: value
      };
      updateTitleAndDescription(req);
      setInitialValue(value);
    }
  };

  const toggle = () => {
    toggleReadOnly();
    if (!readOnly) {
      save();
    } else {
      tiptapRef?.current?.focus();
    }
  };

  const cancel = () => {
    toggleReadOnly();
    setInitialValue("");
    setTimeout(() => {
      setInitialValue(calendarEvent.description?.value);
    }, 100);
  };

  return (
    <div className={styles.container}>
      <Tiptap
        ref={tiptapRef}
        content={initialValue}
        editable={!readOnly}
        placeholder={t("calendarEventDetailDescription")}
        htmlInputId={`${calendarEvent.description?.richTextId}`}
        actionBarMode={"simple"}
      />
      {readOnly && !calendarReadOnly && (
        <Button
          onClick={toggle}
          className={styles.editButton}
          variant={ButtonVariants.filled2}
          data-tooltip-right={t("calendarEventDetailDescriptionEdit")}
        >
          <LuPencil />
        </Button>
      )}

      <div className={styles.actionContainer}>
        {isUpdateLoading && (
          <div className={styles.loadingContainer}>
            <CircularProgress size={14} />
            <div>{t("calendarDescriptionSaving")}</div>
          </div>
        )}

        {!readOnly && (
          <Button
            disabled={isUpdateLoading}
            loading={isUpdateLoading}
            heightVariant={ButtonHeight.short}
            variant={readOnly ? ButtonVariants.filled2 : ButtonVariants.contrast}
            onClick={toggle}
          >
            {t("calendarEventDetailDescriptionSave")}
          </Button>
        )}
        {!readOnly && (
          <Button disabled={isUpdateLoading} heightVariant={ButtonHeight.short} variant={ButtonVariants.filled2}
                  onClick={cancel}>
            {t("calendarEventDetailDescriptionCancel")}
          </Button>
        )}
      </div>
    </div>
  );
};

export default EventDescription;
