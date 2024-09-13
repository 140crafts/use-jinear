import React from "react";
import styles from "./ProjectRow.module.css";
import { ProjectDto } from "@/be/jinear-core";
import { LuBox, LuCalendar, LuCalendarPlus } from "react-icons/lu";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import DatePickerButton from "@/components/datePickerButton/DatePickerButton";
import useTranslation from "@/locals/useTranslation";
import ProjectLeadWorkspaceMember
  from "@/components/workspaceProjectsScreen/projectRow/projectLeadWorkspaceMember/ProjectLeadWorkspaceMember";
import { useUpdateProjectDatesMutation } from "@/api/projectOperationApi";
import ProjectTargetDate from "@/components/workspaceProjectsScreen/projectRow/projectTargetDate/ProjectTargetDate";

interface ProjectRowProps {
  project: ProjectDto;
}

const ProjectRow: React.FC<ProjectRowProps> = ({ project }) => {

  return (
    <div className={styles.container}>
      <Button heightVariant={ButtonHeight.short} className={styles.titleContainer}>
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