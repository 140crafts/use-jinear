"use client";
import React from "react";
import styles from "./page.module.css";
import { useParams } from "next/navigation";
import ProjectFeedScreen from "@/components/projectFeedScreen/ProjectFeedScreen";
import { useRetrievePublicProjectInfoQuery } from "@/api/projectFeedApi";

interface ProjectFeedPageProps {

}

/*
!!!!!!!!!!!!!DEPRECATED!!!!!!!!!!!!
use following instead
/shared/[workspaceUsername]/feed/
* */
const ProjectFeedPage: React.FC<ProjectFeedPageProps> = ({}) => {
  const params = useParams();
  const projectAccessKey: string = params?.projectAccessKey as string;
  const projectId = projectAccessKey?.substring(projectAccessKey?.length - 26, projectAccessKey.length);

  const {
    data: retrievePublicProjectResponse,
    isFetching: isRetrievePublicProjectFetching
  } = useRetrievePublicProjectInfoQuery({ projectId });

  return (
    <div className={styles.container}>
      {projectAccessKey && retrievePublicProjectResponse &&
        <ProjectFeedScreen
          accessKey={projectAccessKey}
          workspaceName={retrievePublicProjectResponse.data.workspaceUsername}
        />
      }
    </div>
  );
};

export default ProjectFeedPage;