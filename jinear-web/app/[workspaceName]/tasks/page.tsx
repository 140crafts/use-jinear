"use client";
import CircularLoading from "@/components/circularLoading/CircularLoading";
import { useRetrieveWorkspaceTeamsQuery } from "@/store/api/teamApi";
import { selectWorkspaceFromWorkspaceUsername } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { useParams, useRouter } from "next/navigation";
import React, { useEffect } from "react";
import styles from "./page.module.css";

interface WorkspaceTasksPageProps {}

const WorkspaceTasksPage: React.FC<WorkspaceTasksPageProps> = ({}) => {
  const router = useRouter();
  const params = useParams();
  const workspaceName: string = params?.workspaceName as string;
  const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));

  const { data: teamsResponse } = useRetrieveWorkspaceTeamsQuery(workspace?.workspaceId || "", {
    skip: workspace == null,
  });
  const team = teamsResponse?.data?.find((team) => team);

  useEffect(() => {
    if (team) {
      router.push(`tasks/${team.username}`);
    }
  }, [team]);

  return (
    <div className={styles.container}>
      <CircularLoading />
    </div>
  );
};

export default WorkspaceTasksPage;
