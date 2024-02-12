import { CalendarEventDto } from "@/model/be/jinear-core";
import React from "react";
import styles from "./EventActionBar.module.css";
import EventSourceCalendarButton from "./eventSourceCalendarButton/EventSourceCalendarButton";

interface EventActionBarProps {
  calendarEvent: CalendarEventDto;
}

const EventActionBar: React.FC<EventActionBarProps> = ({ calendarEvent }) => {
  return (
    <div className={styles.container}>
      <EventSourceCalendarButton calendarEvent={calendarEvent} className={styles.button} />
    </div>
  );
};

export default EventActionBar;
