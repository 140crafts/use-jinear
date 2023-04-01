import Breadcrumb from "@/components/breadcrumb/Breadcrumb";
import BreadcrumbLink from "@/components/breadcrumb/BreadcrumbLink";
import { selectCurrentAccountsPreferredTeam, selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";

interface TaskListScreenBreadcrumbProps {
  type: "active" | "archive" | "backlog" | "all";
}

const TaskListScreenBreadcrumb: React.FC<TaskListScreenBreadcrumbProps> = ({ type }) => {
  const { t } = useTranslation();
  const workspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);
  const team = useTypedSelector(selectCurrentAccountsPreferredTeam);

  const workspaceTitle = workspace?.title || "";
  const workspaceUsername = workspace?.username || "";
  const teamName = team?.name || "";
  const teamUsername = team?.username || "";

  return workspace?.isPersonal ? null : (
    <Breadcrumb>
      <BreadcrumbLink label={workspaceTitle} url={`/${workspaceUsername}`} />
      <BreadcrumbLink label={teamName} url={`/${workspaceUsername}/${teamUsername}`} />
      <BreadcrumbLink label={t(`taskListScreenBreadcrumb_${type}`)} url={`/${workspaceUsername}/${teamUsername}/${type}`} />
    </Breadcrumb>
  );
};

export default TaskListScreenBreadcrumb;
