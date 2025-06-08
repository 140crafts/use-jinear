"use client";
import React from "react";
import styles from "./page.module.css";
import { useParams } from "next/navigation";
import ProjectFeedPostDetailScreen from "@/components/projectFeedPostDetailScreen/ProjectFeedPostDetailScreen";
import { useRetrievePublicProjectInfoWithDomainQuery } from "@/api/projectFeedApi";

interface ProjectPostPageProps {

}

const ProjectPostPage: React.FC<ProjectPostPageProps> = ({}) => {
  const params = useParams();
  const projectPostIdWithSlug: string = params?.projectPostId as string;
  const projectPostId: string = projectPostIdWithSlug ? projectPostIdWithSlug.substring(projectPostIdWithSlug.length - 26, projectPostIdWithSlug.length) : projectPostIdWithSlug;
  const domain = typeof window == "object" && window?.location?.hostname || "";

  const {
    data: retrievePublicProjectResponse,
    isFetching: isRetrievePublicProjectFetching
  } = useRetrievePublicProjectInfoWithDomainQuery({ domain }, { skip: domain == "" });

  const projectId = retrievePublicProjectResponse?.data?.projectId;

  return (
    <div className={styles.container}>
      {projectPostId && projectId && <ProjectFeedPostDetailScreen postId={projectPostId} projectId={projectId} />}
    </div>
  );
};

export default ProjectPostPage;