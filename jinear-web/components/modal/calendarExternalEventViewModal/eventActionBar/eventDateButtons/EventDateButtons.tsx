import DateTimeInput from "@/components/dateTimeInput/DateTimeInput";
import SegmentedControl from "@/components/segmentedControl/SegmentedControl";
import { useToggle } from "@/hooks/useToggle";
import { CalendarEventDto } from "@/model/be/jinear-core";
import useTranslation from "locales/useTranslation";
import React, { useEffect } from "react";
import styles from "./EventDateButtons.module.css";

interface EventDateButtonsProps {
  className?: string;
  calendarEvent: CalendarEventDto;
}

const EventDateButtons: React.FC<EventDateButtonsProps> = ({ calendarEvent }) => {
  const { t } = useTranslation();
  const [allDay, toggleAllDay, setAllDay] = useToggle(calendarEvent.hasPreciseAssignedDate && calendarEvent.hasPreciseDueDate);

  useEffect(() => {
    setAllDay(calendarEvent.hasPreciseAssignedDate && calendarEvent.hasPreciseDueDate);
  }, [calendarEvent.hasPreciseAssignedDate, calendarEvent.hasPreciseDueDate]);

  const changeAllDayToggle = (value: string, index: number) => {
    if (value && (value == "true" || value == "false")) {
      setAllDay?.(value?.toLowerCase() == "true");
    }
  };

  return (
    <div className={styles.container}>
      <div className={styles.datesContainer}>
        <div className={styles.dateInputContainer}>
          <DateTimeInput
            id={`${calendarEvent.calendarEventId}-event-assigned-date"`}
            type={allDay ? "date" : "date-time"}
            // dateInputButtonIcon={IoPlaySkipForwardOutline}
            allowEmptyDate={false}
            initialDate={calendarEvent.assignedDate ? new Date(calendarEvent.assignedDate) : undefined}
            initialDateIsPrecise={!allDay}
            contentContainerClassName={styles.contentContainerClassName}
            dateButtonClassName={styles.dateButtonClassName}
          />
        </div>
        <div>{"->"}</div>
        <div className={styles.dateInputContainer}>
          {/* <span>{t("calendarEventDateEnds")}</span> */}
          <DateTimeInput
            id={`${calendarEvent.calendarEventId}-event-due-date"`}
            type={allDay ? "date" : "date-time"}
            // dateInputButtonIcon={IoPlaySkipBackOutline}
            allowEmptyDate={false}
            initialDate={calendarEvent.dueDate ? new Date(calendarEvent.dueDate) : undefined}
            initialDateIsPrecise={!allDay}
            contentContainerClassName={styles.contentContainerClassName}
            dateButtonClassName={styles.dateButtonClassName}
          />
        </div>
      </div>

      <div className={styles.toggleButtonContainer}>
        <SegmentedControl
          id="calendar-event-allday-toggle-segment-control"
          name="new-team-task-visibility-type-segment-control"
          defaultIndex={["true", "false"].indexOf(`${allDay}`)}
          segments={[
            { label: t("calendarEventAllDayButtonLabel"), value: "true" },
            { label: t("calendarEventSpesificDatesButtonLabel"), value: "false" },
          ]}
          segmentLabelClassName={styles.viewTypeSegmentLabel}
          callback={changeAllDayToggle}
        />
      </div>
    </div>
  );
};

export default EventDateButtons;
