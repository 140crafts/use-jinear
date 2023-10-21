"use client";
import { useParams } from "next/navigation";
import React from "react";
import styles from "./page.module.css";

interface WorkspaceTasksPageProps {}

const WorkspaceTasksPage: React.FC<WorkspaceTasksPageProps> = ({}) => {
  const params = useParams();
  const workspaceName: string = params?.workspaceName as string;

  return <div className={styles.container}>{workspaceName}</div>;
};

export default WorkspaceTasksPage;
