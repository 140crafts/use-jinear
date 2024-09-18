import React from "react";
import styles from "./ProjectDetailScreen.module.css";
import { ProjectDto } from "@/be/jinear-core";
import ProjectTitle from "@/components/projectDetailScreen/projectTitle/ProjectTitle";
import Line from "@/components/line/Line";
import ProjectActionButtons from "@/components/projectDetailScreen/projectActionButtons/ProjectActionButtons";
import ProjectDescription from "@/components/projectDetailScreen/projectDescription/ProjectDescription";
import ProjectDetailMilestones from "@/components/projectDetailScreen/projectDetailMilestones/ProjectDetailMilestones";

interface ProjectDetailScreenProps {
  project: ProjectDto;
  isFetching: boolean;
}

const ProjectDetailScreen: React.FC<ProjectDetailScreenProps> = ({ project, isFetching }) => {

  return (
    <div className={styles.container}>
      <ProjectTitle projectId={project.projectId} title={project.title} isFetching={isFetching} />
      <Line />
      <ProjectActionButtons project={project} isFetching={isFetching} />
      <Line />
      <ProjectDescription projectId={project.projectId} description={project.description} isFetching={isFetching} />
      <Line />
      <ProjectDetailMilestones project={project} />
    </div>
  );
};

export default ProjectDetailScreen;