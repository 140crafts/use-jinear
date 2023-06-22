import Breadcrumb from "@/components/breadcrumb/Breadcrumb";
import BreadcrumbLink from "@/components/breadcrumb/BreadcrumbLink";
import { TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import useTranslation from "locales/useTranslation";
import React from "react";

interface TaskBoardListBreadCrumbProps {
  workspace: WorkspaceDto;
  team: TeamDto;
}

const TaskBoardListBreadCrumb: React.FC<TaskBoardListBreadCrumbProps> = ({ workspace, team }) => {
  const { t } = useTranslation();
  return (
    <Breadcrumb>
      <BreadcrumbLink label={workspace.title} url={`/${workspace.username}`} />
      <BreadcrumbLink label={team.name} url={`/${workspace?.username}/${team.username}`} />
      <BreadcrumbLink
        label={t("taskBoardListScreenBreadcrumbLabel")}
        url={`/${workspace.username}/${team.username}/task-boards`}
      />
    </Breadcrumb>
  );
};

export default TaskBoardListBreadCrumb;
