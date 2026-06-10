"use client";
import DeleteCalendarCard from "@/components/calendarSettingsPage/deleteCalendarCard/DeleteCalendarCard";
import { useParams } from "next/navigation";
import React from "react";
import styles from "./page.module.css";

interface CalendarSettingsPageProps {}

const CalendarSettingsPage: React.FC<CalendarSettingsPageProps> = ({}) => {
  const params = useParams();
  const calendarId: string = params?.calendarId as string;

  return <div className={styles.container}>{calendarId && <DeleteCalendarCard calendarId={calendarId} />}</div>;
};

export default CalendarSettingsPage;
