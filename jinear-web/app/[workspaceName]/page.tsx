"use client";
import Calendar from "@/components/calendar/Calendar";
import { selectWorkspaceFromWorkspaceUsername } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { useParams } from "next/navigation";
import React from "react";
import styles from "./index.module.css";

interface WorkspacePageProps {}

const WorkspacePage: React.FC<WorkspacePageProps> = ({}) => {
  const params = useParams();
  const workspaceName: string = params?.workspaceName as string;
  const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));

  return <div className={styles.container}>{workspace && <Calendar className={styles.calendar} workspace={workspace} />}</div>;
};
export default WorkspacePage;
