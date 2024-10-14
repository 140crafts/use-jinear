"use client";
import React from "react";
import styles from "./page.module.css";
import { useParams } from "next/navigation";
import ProjectFeedPostDetailScreen from "@/components/projectFeedPostDetailScreen/ProjectFeedPostDetailScreen";

interface ProjectPostPageProps {

}

const ProjectPostPage: React.FC<ProjectPostPageProps> = ({}) => {
  const params = useParams();
  const projectAccessKey: string = params?.projectAccessKey as string;
  const projectPostId: string = params?.projectPostId as string;
  const projectId = projectAccessKey?.substring(projectAccessKey?.length - 26, projectAccessKey.length);

  return (
    <div className={styles.container}>
      {projectPostId && projectAccessKey &&
        <ProjectFeedPostDetailScreen
          accessKey={projectAccessKey}
          postId={projectPostId}
          projectId={projectId}
        />}
    </div>
  );
};

export default ProjectPostPage;