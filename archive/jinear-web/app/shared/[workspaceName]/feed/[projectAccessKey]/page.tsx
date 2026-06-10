"use client";
import React from "react";
import styles from "./page.module.css";
import { useParams } from "next/navigation";
import ProjectFeedScreen from "@/components/projectFeedScreen/ProjectFeedScreen";

interface ProjectFeedPageProps {

}

const ProjectFeedPage: React.FC<ProjectFeedPageProps> = ({}) => {
  const params = useParams();
  const projectAccessKey: string = params?.projectAccessKey as string;
  const workspaceName: string = params?.workspaceName as string;

  return (
    <div className={styles.container}>
      {projectAccessKey && <ProjectFeedScreen accessKey={projectAccessKey} workspaceName={workspaceName} />}
    </div>
  );
};

export default ProjectFeedPage;