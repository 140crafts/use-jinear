import React from "react";
import styles from "./layout.module.scss";
import ProjectFeedLayoutHeader from "@/components/projectFeedLayoutHeader/ProjectFeedLayoutHeader";

interface ProjectFeedLayoutProps {
  children: React.ReactNode;
}

const ProjectFeedLayout: React.FC<ProjectFeedLayoutProps> = ({ children }) => {

  return (
    <div id="project-feed-layout-container" className={styles.container}>
      <div id="project-feed-layout-header" className={styles.header}>
        <ProjectFeedLayoutHeader />
      </div>
      <div id="project-feed-layout-content" className={styles.content}>
        <div id="workspace-layout-page-content" className={styles.pageContent}>
          {children}
        </div>
      </div>
    </div>
  );
};

export default ProjectFeedLayout;