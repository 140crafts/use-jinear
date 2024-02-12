"use client";
import CalendarMemberList from "@/components/calendarMembersPage/calendarMemberList/CalendarMemberList";
import { selectWorkspaceFromWorkspaceUsername } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { useParams } from "next/navigation";
import React from "react";
import styles from "./page.module.css";

interface CalendarMembersPageProps {}

const CalendarMembersPage: React.FC<CalendarMembersPageProps> = ({}) => {
  const params = useParams();
  const workspaceName: string = params?.workspaceName as string;
  const calendarId: string = params?.calendarId as string;
  const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));
  return (
    <div className={styles.container}>
      {workspace && <CalendarMemberList calendarId={calendarId} workspaceId={workspace.workspaceId} />}
    </div>
  );
};

export default CalendarMembersPage;
