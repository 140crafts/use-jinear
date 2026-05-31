import Calendar from "@/components/calendar/Calendar";
import { selectWorkspaceFromWorkspaceUsername } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store";
import React from "react";
import styles from "./page.module.css";
import {useParams} from "react-router-dom";

interface CalendarPageProps {}

const CalendarPage: React.FC<CalendarPageProps> = ({}) => {
  const {workspaceName} = useParams();
  const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));

  return <div className={styles.container}>{workspace && <Calendar className={styles.calendar} workspace={workspace} />}</div>;
};

export default CalendarPage;
