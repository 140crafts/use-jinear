import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import DateTimeInput from "@/components/dateTimeInput/DateTimeInput";
import { useToggle } from "@/hooks/useToggle";
import { CalendarEventDto } from "@/model/be/jinear-core";
import { useUpdateCalendarEventDatesMutation } from "@/store/api/calendarEventApi";
import Logger from "@/utils/logger";
import { CircularProgress } from "@mui/material";
import { isBefore } from "date-fns";
import useTranslation from "locales/useTranslation";
import React, { useEffect, useState } from "react";
import styles from "./EventDateButtons.module.css";

interface EventDateButtonsProps {
  className?: string;
  calendarEvent: CalendarEventDto;
}

const logger = Logger("EventDateButtons");

const EventDateButtons: React.FC<EventDateButtonsProps> = ({ calendarEvent }) => {
  const { t } = useTranslation();

  const [initialAllDay, setInitialAllDay] = useState(calendarEvent.hasPreciseAssignedDate && calendarEvent.hasPreciseDueDate);
  const [initialDates, setInitialDates] = useState({
    assignedDate: isBefore(new Date(calendarEvent?.assignedDate), new Date(calendarEvent?.dueDate))
      ? new Date(calendarEvent?.assignedDate)
      : new Date(calendarEvent?.dueDate),
    dueDate: isBefore(new Date(calendarEvent?.assignedDate), new Date(calendarEvent?.dueDate))
      ? new Date(calendarEvent?.dueDate)
      : new Date(calendarEvent?.assignedDate),
  });

  const [dates, setDates] = useState(initialDates);
  const [allDay, toggleAllDay, setAllDay] = useToggle(initialAllDay);

  const [updateCalendarEventDates, { isLoading }] = useUpdateCalendarEventDatesMutation();

  logger.log({ allDay, dates });

  useEffect(() => {
    setAllDay(calendarEvent.hasPreciseAssignedDate && calendarEvent.hasPreciseDueDate);
  }, [calendarEvent.hasPreciseAssignedDate, calendarEvent.hasPreciseDueDate]);

  useEffect(() => {
    if (isBefore(dates.dueDate, dates.assignedDate)) {
      const nextAssignedDate = dates.dueDate;
      const nextDueDate = dates.assignedDate;
      setDates({ assignedDate: nextAssignedDate, dueDate: nextDueDate });
    }
  }, [JSON.stringify(dates)]);

  const changeAssignedDate = (date?: Date) => {
    if (date) {
      setDates({ ...dates, assignedDate: date });
    }
  };

  const changeDueDate = (date?: Date) => {
    if (date) {
      setDates({ ...dates, dueDate: date });
    }
  };

  const handleChecked = (event: React.ChangeEvent<HTMLInputElement>) => {
    setAllDay(event.target.checked);
  };

  const saveDates = () => {
    if (calendarEvent.externalCalendarSourceDto) {
      updateCalendarEventDates({
        calendarId: calendarEvent.calendarId,
        calendarSourceId: calendarEvent.externalCalendarSourceDto.externalCalendarSourceId,
        calendarEventId: calendarEvent.calendarEventId,
        assignedDate: dates.assignedDate,
        dueDate: dates.dueDate,
        hasPreciseAssignedDate: allDay,
        hasPreciseDueDate: allDay,
      });
      setInitialDates({ assignedDate: dates.assignedDate, dueDate: dates.dueDate });
      setInitialAllDay(allDay);
    }
  };

  const changeToInitial = () => {
    setDates(initialDates);
    setAllDay(initialAllDay);
  };

  return (
    <div className={styles.container}>
      <div className={styles.datesContainer}>
        <div className={styles.dateInputContainer}>
          <DateTimeInput
            id={`${calendarEvent.calendarEventId}-event-assigned-date"`}
            type={allDay ? "date" : "date-time"}
            allowEmptyDate={false}
            value={dates.assignedDate}
            setValue={changeAssignedDate}
            valuePrecise={allDay}
            toggleValuePrecise={toggleAllDay}
            setValuePrecise={setAllDay}
            contentContainerClassName={styles.contentContainerClassName}
            dateButtonClassName={styles.dateButtonClassName}
          />
        </div>
        <div>{"->"}</div>
        <div className={styles.dateInputContainer}>
          <DateTimeInput
            id={`${calendarEvent.calendarEventId}-event-due-date"`}
            type={allDay ? "date" : "date-time"}
            allowEmptyDate={false}
            value={dates.dueDate}
            setValue={changeDueDate}
            valuePrecise={allDay}
            toggleValuePrecise={toggleAllDay}
            setValuePrecise={setAllDay}
            contentContainerClassName={styles.contentContainerClassName}
            dateButtonClassName={styles.dateButtonClassName}
          />
        </div>
      </div>

      <div className={styles.toggleButtonContainer}>
        <input id={"all-day-checkbox"} type="checkbox" checked={allDay} onChange={handleChecked} />
        <label htmlFor={"all-day-checkbox"}>{t("calendarEventAllDayButtonLabel")}</label>
      </div>

      {isLoading && (
        <div className={styles.loadingContainer}>
          <CircularProgress size={14} />
          <div>{t("calendarDatesSaving")}</div>
        </div>
      )}

      {(JSON.stringify(initialDates) != JSON.stringify(dates) || `${initialAllDay}` != `${allDay}`) && (
        <div className={styles.actionsContainer}>
          {!isLoading && (
            <>
              <Button
                onClick={saveDates}
                disabled={isLoading}
                loading={isLoading}
                heightVariant={ButtonHeight.short}
                variant={ButtonVariants.contrast}
              >
                {t("calendarDatesSaveButton")}
              </Button>
              <Button
                disabled={isLoading}
                onClick={changeToInitial}
                heightVariant={ButtonHeight.short}
                variant={ButtonVariants.filled2}
              >
                {t("calendarDatesCancelButton")}
              </Button>
            </>
          )}
        </div>
      )}
    </div>
  );
};

export default EventDateButtons;
