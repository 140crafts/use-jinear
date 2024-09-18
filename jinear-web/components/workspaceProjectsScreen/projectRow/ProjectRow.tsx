import React from "react";
import styles from "./ProjectRow.module.css";
import { ProjectDto } from "@/be/jinear-core";
import { LuBox } from "react-icons/lu";
import Button, { ButtonHeight } from "@/components/button";
import ProjectLeadWorkspaceMember
  from "@/components/workspaceProjectsScreen/projectRow/projectLeadWorkspaceMember/ProjectLeadWorkspaceMember";
import ProjectTargetDate from "@/components/workspaceProjectsScreen/projectRow/projectTargetDate/ProjectTargetDate";

interface ProjectRowProps {
  project: ProjectDto;
}

const ProjectRow: React.FC<ProjectRowProps> = ({ project }) => {

  return (
    <div className={styles.container}>
      <Button
        heightVariant={ButtonHeight.short}
        className={styles.titleContainer}
        href={`/${project.workspace.username}/tasks/projects/${project.projectId}`}>
        <LuBox className={"icon"} />
        <b className={"line-clamp"}>{project.title}</b>
      </Button>
      <div className={styles.infoContainer}>
        <ProjectTargetDate project={project} />
      </div>
      <div className={styles.infoContainer}>
        <ProjectLeadWorkspaceMember
          workspaceId={project.workspaceId}
          projectId={project.projectId}
          leadWorkspaceMember={project.leadWorkspaceMember}
        />
      </div>
    </div>
  );
};

export default ProjectRow;