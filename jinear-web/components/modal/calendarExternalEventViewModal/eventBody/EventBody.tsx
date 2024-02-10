import Line from "@/components/line/Line";
import { CalendarEventDto } from "@/model/be/jinear-core";
import React from "react";
import EventDescription from "../eventDescription/EventDescription";
import EventTitle from "../eventTitle/EventTitle";
import styles from "./EventBody.module.css";

interface EventBodyProps {
  calendarEvent: CalendarEventDto;
}

const EventBody: React.FC<EventBodyProps> = ({ calendarEvent }) => {
  return (
    <div className={styles.container}>
      <EventTitle title={calendarEvent?.title} />
      <EventDescription description={calendarEvent?.description} />
      <Line />
    </div>
  );
};

export default EventBody;
