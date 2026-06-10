"use client";
import React from "react";
import styles from "./index.module.css";
import useTranslation from "@/locals/useTranslation";
import { useParams } from "next/navigation";
import { useTypedSelector } from "@/store/store";
import { selectWorkspaceFromWorkspaceUsername } from "@/slice/accountSlice";
import { WorkspaceDto } from "@/be/jinear-core";
import WorkspaceProjectsScreen from "@/components/workspaceProjectsScreen/WorkspaceProjectsScreen";

interface ProjectsPageProps {

}

const ProjectsPage: React.FC<ProjectsPageProps> = ({}) => {
  const { t } = useTranslation();
  const params = useParams();
  const workspaceName: string = params?.workspaceName as string;

  const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName)) as WorkspaceDto;
  return (
    <div className={styles.container}>
      {workspace && <WorkspaceProjectsScreen workspace={workspace} />}
    </div>
  );
};

export default ProjectsPage;