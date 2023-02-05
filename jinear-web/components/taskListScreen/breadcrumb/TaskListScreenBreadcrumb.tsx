import Breadcrumb from "@/components/breadcrumb/Breadcrumb";
import BreadcrumbLink from "@/components/breadcrumb/BreadcrumbLink";
import {
  selectCurrentAccountsPreferredTeam,
  selectCurrentAccountsPreferredWorkspace,
} from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";

interface TaskListScreenBreadcrumbProps {
  type: "active" | "archive" | "backlog";
}

const TaskListScreenBreadcrumb: React.FC<TaskListScreenBreadcrumbProps> = ({
  type,
}) => {
  const { t } = useTranslation();
  const workspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);
  const team = useTypedSelector(selectCurrentAccountsPreferredTeam);

  const workspaceUsername = workspace?.username || "";
  const teamName = team?.name || "";
  const workspaceUsernameEncoded = encodeURI(workspaceUsername);
  const teamNameEncoded = encodeURI(teamName);

  return (
    <Breadcrumb>
      <BreadcrumbLink
        label={workspaceUsername}
        url={`/${workspaceUsernameEncoded}`}
      />
      <BreadcrumbLink
        label={teamName}
        url={`/${workspaceUsernameEncoded}/${teamNameEncoded}`}
      />
      <BreadcrumbLink
        label={t(`taskListScreenBreadcrumb_${type}`)}
        url={`/${workspaceUsernameEncoded}/${teamNameEncoded}/task-list`}
      />
    </Breadcrumb>
  );
};

export default TaskListScreenBreadcrumb;
