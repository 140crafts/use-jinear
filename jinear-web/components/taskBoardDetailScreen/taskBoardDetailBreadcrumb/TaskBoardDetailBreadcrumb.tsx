import Breadcrumb from "@/components/breadcrumb/Breadcrumb";
import BreadcrumbLink from "@/components/breadcrumb/BreadcrumbLink";
import { TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import useTranslation from "locales/useTranslation";
import React from "react";

interface TaskBoardDetailBreadcrumbProps {
  workspace: WorkspaceDto;
  team: TeamDto;

  title: string;
  taskBoardId: string;
}

const TaskBoardDetailBreadcrumb: React.FC<TaskBoardDetailBreadcrumbProps> = ({ workspace, team, title, taskBoardId }) => {
  const { t } = useTranslation();

  return !workspace.isPersonal ? (
    <Breadcrumb>
      <BreadcrumbLink label={workspace.title} url={`/${workspace.username}`} />
      <BreadcrumbLink label={team.name} url={`/${workspace.username}/${team.username}`} />
      <BreadcrumbLink
        label={t("taskBoardListScreenBreadcrumbLabel")}
        url={`/${workspace.username}/${team.username}/task-boards`}
      />
      <BreadcrumbLink label={title} url={`/${workspace.username}/${team.username}/task-boards/${taskBoardId}`} />
    </Breadcrumb>
  ) : null;
};

export default TaskBoardDetailBreadcrumb;
