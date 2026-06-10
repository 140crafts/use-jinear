import React, { useEffect, useState } from "react";
import styles from "./ExistingEventDateButtons.module.css";
import { useUpdateCalendarEventDatesMutation } from "@/api/calendarEventApi";
import { CalendarEventDto } from "@/be/jinear-core";
import { useToggle } from "@/hooks/useToggle";
import { isBefore, isSameSecond } from "date-fns";
import EventDateButtons from "@/components/eventDateButtons2/EventDateButtons";
import { CircularProgress } from "@mui/material";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import useTranslation from "@/locals/useTranslation";
import Logger from "@/utils/logger";

interface ExistingEventDateButtonsProps {
  calendarEvent: CalendarEventDto;
}

const logger = Logger("ExistingEventDateButtons");

const ExistingEventDateButtons: React.FC<ExistingEventDateButtonsProps> = ({ calendarEvent }) => {
  const { t } = useTranslation();
  const calendarReadOnly = calendarEvent?.externalCalendarSourceDto?.readOnly;
  const [initialDates, setInitialDates] = useState({
    assignedDate: (calendarEvent.assignedDate && calendarEvent.dueDate) ? isBefore(new Date(calendarEvent.assignedDate), new Date(calendarEvent.dueDate))
      ? new Date(calendarEvent.assignedDate)
      : new Date(calendarEvent.dueDate) : new Date(),
    dueDate: (calendarEvent.assignedDate && calendarEvent.dueDate) ? isBefore(new Date(calendarEvent.assignedDate), new Date(calendarEvent.dueDate))
      ? new Date(calendarEvent.dueDate)
      : new Date(calendarEvent.assignedDate) : new Date(),
    allDay: !(calendarEvent.hasPreciseAssignedDate || calendarEvent.hasPreciseDueDate)
  });

  const [allDay, toggleAllDay, setAllDay] = useToggle(!(calendarEvent.hasPreciseAssignedDate || calendarEvent.hasPreciseDueDate));

  const [assignedDate, setAssignedDate] = useState((calendarEvent.assignedDate && calendarEvent.dueDate) ? isBefore(new Date(calendarEvent.assignedDate), new Date(calendarEvent.dueDate))
    ? new Date(calendarEvent.assignedDate)
    : new Date(calendarEvent.dueDate) : new Date());

  const [dueDate, setDueDate] = useState((calendarEvent.assignedDate && calendarEvent.dueDate) ? isBefore(new Date(calendarEvent.assignedDate), new Date(calendarEvent.dueDate))
    ? new Date(calendarEvent.dueDate)
    : new Date(calendarEvent.assignedDate) : new Date());

  logger.log({
    allDay,
    assignedDate,
    dueDate,
    calendarEvent,
    initialDates,
    assignedDateComp: initialDates.assignedDate?.toISOString() != assignedDate?.toISOString(),
    dueDateComp: initialDates.dueDate?.toISOString() != dueDate?.toISOString(),
    allDayComp: `${initialDates.allDay}` != `${allDay}`
  });

  const [updateCalendarEventDates, { isLoading: isUpdateDatesLoading }] = useUpdateCalendarEventDatesMutation();

  useEffect(() => {
    if ((initialDates.assignedDate?.toISOString() != assignedDate?.toISOString() || initialDates.dueDate?.toISOString() != dueDate?.toISOString() || `${initialDates.allDay}` != `${allDay}`)) {
      if (calendarEvent && calendarEvent?.externalCalendarSourceDto) {
        updateCalendarEventDates({
          calendarId: calendarEvent.calendarId,
          calendarSourceId: calendarEvent.externalCalendarSourceDto.externalCalendarSourceId,
          calendarEventId: calendarEvent.calendarEventId,
          assignedDate,
          dueDate,
          hasPreciseAssignedDate: allDay,
          hasPreciseDueDate: allDay
        });
        setInitialDates({ allDay, assignedDate, dueDate });
      }
    }
  }, [updateCalendarEventDates, calendarEvent, initialDates, assignedDate, dueDate, allDay]);

  const onAssignedDateUpdate = (date: Date) => {
    setAssignedDate(date);
  };

  const onDueDateUpdate = (date: Date) => {
    setDueDate(date);
  };

  const onAllDayUpdate = (allDay: boolean) => {
    setAllDay(!allDay);
  };

  return (
    <div className={styles.container}>
      <EventDateButtons
        allDay={allDay}
        assignedDate={assignedDate}
        dueDate={dueDate}
        onAssignedDateUpdate={onAssignedDateUpdate}
        onDueDateUpdate={onDueDateUpdate}
        onAllDayUpdate={onAllDayUpdate}
        disabled={!!calendarReadOnly}
      />
    </div>
  );
};

export default ExistingEventDateButtons;