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

  return (
    <Breadcrumb>
      <BreadcrumbLink
        label={workspace?.title || ""}
        url={`/${workspace?.username}`}
      />
      <BreadcrumbLink
        label={team?.name || ""}
        url={`/${workspace?.username}/${team?.name}`}
      />
      <BreadcrumbLink
        label={t(`taskListScreenBreadcrumb_${type}`)}
        url={`/${workspace?.username}/${team?.name}/task-list`}
      />
    </Breadcrumb>
  );
};

export default TaskListScreenBreadcrumb;
