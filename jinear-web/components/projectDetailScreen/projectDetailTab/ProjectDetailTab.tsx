import React from "react";
import styles from "./ProjectDetailTab.module.css";
import { ProjectDto } from "@/be/jinear-core";
import ProjectTitle from "@/components/projectDetailScreen/projectTitle/ProjectTitle";
import Line from "@/components/line/Line";
import ProjectActionButtons from "@/components/projectDetailScreen/projectActionButtons/ProjectActionButtons";
import ProjectDescription from "@/components/projectDetailScreen/projectDescription/ProjectDescription";
import ProjectDetailMilestones from "@/components/projectDetailScreen/projectDetailMilestones/ProjectDetailMilestones";
import ProjectArchivedInfo from "@/components/projectDetailScreen/projectArchivedInfo/ProjectArchivedInfo";
import cn from "classnames";

interface ProjectDetailTabProps {
  project: ProjectDto;
  isFetching: boolean;
}

const ProjectDetailTab: React.FC<ProjectDetailTabProps> = ({ project, isFetching }) => {

  return (
    <div className={styles.container}>

      <ProjectArchivedInfo project={project} />

      <div className={cn(styles.contentContainer, project.archived && styles.archived)}>
        <ProjectTitle projectId={project.projectId} title={project.title} isFetching={isFetching} />
        <Line />
        <ProjectActionButtons project={project} isFetching={isFetching} />
        <Line />
        <ProjectDescription projectId={project.projectId} description={project.description} isFetching={isFetching} />
        <Line />
        <ProjectDetailMilestones project={project} />
      </div>
    </div>
  );
};

export default ProjectDetailTab;