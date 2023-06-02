import Breadcrumb from "@/components/breadcrumb/Breadcrumb";
import BreadcrumbLink from "@/components/breadcrumb/BreadcrumbLink";
import { selectCurrentAccountsPreferredTeam, selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";

interface TaskBoardListBreadCrumbProps {}

const TaskBoardListBreadCrumb: React.FC<TaskBoardListBreadCrumbProps> = ({}) => {
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
        label={t("taskBoardListScreenBreadcrumbLabel")}
        url={`/${currentWorkspace?.username || ""}/${currentTeam?.username || ""}/task-boards`}
      />
    </Breadcrumb>
  );
};

export default TaskBoardListBreadCrumb;
