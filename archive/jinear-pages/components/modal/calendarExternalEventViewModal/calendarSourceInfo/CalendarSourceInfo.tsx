import { ExternalCalendarSourceDto } from "@/model/be/jinear-core";
import React from "react";
import styles from "./CalendarSourceInfo.module.css";

interface CalendarSourceInfoProps {
  calendarSource: ExternalCalendarSourceDto;
}

const CalendarSourceInfo: React.FC<CalendarSourceInfoProps> = ({ calendarSource }) => {
  return <div className={styles.container}></div>;
};

export default CalendarSourceInfo;
