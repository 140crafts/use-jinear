"use client";
import React from "react";
import styles from "./page.module.css";
import { useParams } from "next/navigation";
import ProjectFeedPostDetailScreen from "@/components/projectFeedPostDetailScreen/ProjectFeedPostDetailScreen";
import { useRetrievePublicProjectInfoQuery } from "@/api/projectFeedApi";

interface ProjectPostPageProps {

}

/*
!!!!!!!!!!!!!DEPRECATED!!!!!!!!!!!!
use following instead
/public/[workspaceUsername]/feed/
* */

const ProjectPostPage: React.FC<ProjectPostPageProps> = ({}) => {
  const params = useParams();
  const projectAccessKey: string = params?.projectAccessKey as string;
  const projectPostId: string = params?.projectPostId as string;
  const projectId = projectAccessKey?.substring(projectAccessKey?.length - 26, projectAccessKey.length);

  const {
    data: retrievePublicProjectResponse,
    isFetching: isRetrievePublicProjectFetching
  } = useRetrievePublicProjectInfoQuery({ projectId });

  return (
    <div className={styles.container}>
      {projectPostId && projectAccessKey && retrievePublicProjectResponse &&
        <ProjectFeedPostDetailScreen
          accessKey={projectAccessKey}
          postId={projectPostId}
          projectId={projectId}
          workspaceName={retrievePublicProjectResponse.data.workspaceUsername}
        />}
    </div>
  );
};

export default ProjectPostPage;