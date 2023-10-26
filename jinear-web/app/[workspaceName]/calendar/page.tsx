"use client";
import Calendar from "@/components/calendar/Calendar";
import { selectWorkspaceFromWorkspaceUsername } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { useParams } from "next/navigation";
import React from "react";
import styles from "./page.module.css";

interface CalendarPageProps {}

const CalendarPage: React.FC<CalendarPageProps> = ({}) => {
  const params = useParams();
  const workspaceName: string = params?.workspaceName as string;
  const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));

  return (
    <div className={styles.container}>
      {workspace != null ? <Calendar className={styles.calendar} workspace={workspace} /> : <>Loading</>}
    </div>
  );
};

export default CalendarPage;
