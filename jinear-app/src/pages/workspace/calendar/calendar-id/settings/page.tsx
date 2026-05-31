import DeleteCalendarCard from "@/components/calendarSettingsPage/deleteCalendarCard/DeleteCalendarCard";
import React from "react";
import styles from "./page.module.css";
import {useParams} from "react-router-dom";

interface CalendarSettingsPageProps {
}

const CalendarSettingsPage: React.FC<CalendarSettingsPageProps> = ({}) => {
    const {calendarId} = useParams();
    return <div className={styles.container}>{calendarId && <DeleteCalendarCard calendarId={calendarId}/>}</div>;
};

export default CalendarSettingsPage;
