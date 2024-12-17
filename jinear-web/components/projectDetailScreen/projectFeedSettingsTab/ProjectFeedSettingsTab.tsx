import React from "react";
import styles from "./ProjectFeedSettingsTab.module.css";
import { ProjectDto } from "@/be/jinear-core";
import cn from "classnames";
import Line from "@/components/line/Line";
import ProjectFeedInfo from "@/components/projectDetailScreen/projectFeedSettingsTab/projectFeedInfo/ProjectFeedInfo";
import ProjectFeedAccessTypeSelect
  from "@/components/projectDetailScreen/projectFeedSettingsTab/projectFeedAccessTypeSelect/ProjectFeedAccessTypeSelect";
import ProjectPostInitializeAccessTypeSelect
  from "@/components/projectDetailScreen/projectFeedSettingsTab/projectPostInitializeAccessTypeSelect/ProjectPostInitializeAccessTypeSelect";
import { useRetrieveProjectPermissionsQuery } from "@/api/projectQueryApi";
import ProjectPostCommentPolicyTypeSelect
  from "@/components/projectDetailScreen/projectFeedSettingsTab/projectPostCommentPolicyTypeSelect/ProjectPostCommentPolicyTypeSelect";
import ProjectDomainList
  from "@/components/projectDetailScreen/projectFeedSettingsTab/projectDomainList/ProjectDomainList";

interface ProjectFeedSettingsTabProps {
  project: ProjectDto;
  isFetching: boolean;
}

const ProjectFeedSettingsTab: React.FC<ProjectFeedSettingsTabProps> = ({ project, isFetching }) => {
  const { data: projectPermissionsResponse } = useRetrieveProjectPermissionsQuery({ projectId: project.projectId });
  const hasExplicitAdminAccess = projectPermissionsResponse?.data.accountIsProjectTeamsAdmin || projectPermissionsResponse?.data.accountWorkspaceAdminOrOwner;

  return (
    <div className={cn(styles.container, project.archived && styles.archived)}>
      <ProjectDomainList
        project={project}
        hasExplicitAdminAccess={hasExplicitAdminAccess}
      />
      <Line />
      <ProjectFeedInfo
        projectFeedSettings={project.projectFeedSettings}
        isFetching={isFetching}
        editable={hasExplicitAdminAccess} />
      <Line />
      <ProjectFeedAccessTypeSelect
        projectFeedSettings={project.projectFeedSettings}
        isFetching={isFetching}
        editable={hasExplicitAdminAccess} />
      <Line />
      <ProjectPostInitializeAccessTypeSelect
        projectFeedSettings={project.projectFeedSettings}
        isFetching={isFetching}
        editable={hasExplicitAdminAccess}
      />
      <Line />
      <ProjectPostCommentPolicyTypeSelect
        projectFeedSettings={project.projectFeedSettings}
        isFetching={isFetching}
        editable={hasExplicitAdminAccess}
      />
    </div>
  );
};

export default ProjectFeedSettingsTab;