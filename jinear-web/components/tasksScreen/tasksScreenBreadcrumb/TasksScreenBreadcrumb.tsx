import Breadcrumb from "@/components/breadcrumb/Breadcrumb";
import BreadcrumbLink from "@/components/breadcrumb/BreadcrumbLink";
import { TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import useTranslation from "locales/useTranslation";
import React from "react";

interface TasksScreenBreadcrumbProps {
  workspace: WorkspaceDto;
  team: TeamDto;
}

const TasksScreenBreadcrumb: React.FC<TasksScreenBreadcrumbProps> = ({ workspace, team }) => {
  const { t } = useTranslation();
  return workspace?.isPersonal ? null : (
    <Breadcrumb>
      <BreadcrumbLink label={workspace.title} url={`/${workspace.username}`} />
      <BreadcrumbLink label={team.name} url={`/${workspace.username}/${team.username}`} />
      <BreadcrumbLink label={t(`tasksScreenBreadcrumbLabel`)} url={`/${workspace.username}/${team.username}/tasks`} />
    </Breadcrumb>
  );
};

export default TasksScreenBreadcrumb;
