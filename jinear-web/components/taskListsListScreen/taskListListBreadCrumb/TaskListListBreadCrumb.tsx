import Breadcrumb from "@/components/breadcrumb/Breadcrumb";
import BreadcrumbLink from "@/components/breadcrumb/BreadcrumbLink";
import { selectCurrentAccountsPreferredTeam, selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";

interface TaskListListBreadCrumbProps {}

const TaskListListBreadCrumb: React.FC<TaskListListBreadCrumbProps> = ({}) => {
  const { t } = useTranslation();
  const currentWorkspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);
  const currentTeam = useTypedSelector(selectCurrentAccountsPreferredTeam);

  return (
    <Breadcrumb>
      <BreadcrumbLink label={currentWorkspace?.title || ""} url={`/${currentWorkspace?.username || ""}`} />
      <BreadcrumbLink
        label={currentTeam?.name || ""}
        url={`/${currentWorkspace?.username || ""}/${currentTeam?.username || ""}`}
      />
      <BreadcrumbLink
        label={t("taskListListScreenBreadcrumbLabel")}
        url={`/${currentWorkspace?.username || ""}/${currentTeam?.username || ""}/task-lists`}
      />
    </Breadcrumb>
  );
};

export default TaskListListBreadCrumb;
