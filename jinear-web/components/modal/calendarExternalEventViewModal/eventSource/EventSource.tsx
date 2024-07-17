import { CalendarEventDto } from "@/model/be/jinear-core";
import React, { useState } from "react";
import styles from "./EventActionBar.module.css";
import ExternalCalendarAndSourcePicker
  from "@/components/externalCalendarAndSourcePicker/ExternalCalendarAndSourcePicker";
import { useRetrieveCalendarMembershipsQuery } from "@/api/calendarMemberApi";
import Logger from "@/utils/logger";
import { useMoveEventMutation } from "@/api/calendarEventApi";

interface EventSourceProps {
  calendarEvent: CalendarEventDto;
}

const logger = Logger("EventActionBar");

const EventSource: React.FC<EventSourceProps> = ({ calendarEvent }) => {
  const [calendarId, setCalendarId] = useState<string>(calendarEvent.calendarId);
  const [calendarSourceId, setCalendarSourceId] = useState<string | undefined>(calendarEvent.externalCalendarSourceDto?.externalCalendarSourceId);

  const { data: membershipsResponse } = useRetrieveCalendarMembershipsQuery({ workspaceId: calendarEvent.workspaceId });
  const [moveEvent, {}] = useMoveEventMutation();

  const onCalendarSelect = (calendarId: string) => {
    setCalendarId(calendarId);
  };

  const onCalendarSourceSelect = (calendarSourceId: string) => {
    logger.log({ onCalendarSourceSelect: calendarSourceId });
    setCalendarSourceId(calendarSourceId);
    if (calendarEvent.externalCalendarSourceDto?.externalCalendarSourceId) {
      moveEvent({
        calendarId: calendarEvent.calendarId,
        calendarSourceId: calendarEvent.externalCalendarSourceDto?.externalCalendarSourceId,
        targetCalendarSourceId: calendarSourceId,
        eventId: calendarEvent.calendarEventId
      });
    }
  };

  return (
    <div className={styles.container}>
      {calendarId && calendarSourceId &&
        <ExternalCalendarAndSourcePicker
          calendarList={membershipsResponse?.data?.map(membership => membership.calendar) ?? []}
          selectedCalendarId={calendarId}
          selectedCalendarSourceId={calendarSourceId}
          onCalendarSelect={onCalendarSelect}
          onCalendarSourceSelect={onCalendarSourceSelect}
          hasNewEventText={false}
          calendarChangeable={false}
        />}
    </div>
  );
};

export default EventSource;
