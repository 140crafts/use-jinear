"use client";
import React from "react";
import styles from "./page.module.css";
import ProjectFeedScreen from "@/components/projectFeedScreen/ProjectFeedScreen";

interface ProjectFeedPageProps {

}

const ProjectFeedPage: React.FC<ProjectFeedPageProps> = ({}) => {
  return (
    <div className={styles.container}>
      <ProjectFeedScreen />
    </div>
  );
};

export default ProjectFeedPage;