import Breadcrumb from "@/components/breadcrumb/Breadcrumb";
import BreadcrumbLink from "@/components/breadcrumb/BreadcrumbLink";
import { selectCurrentAccountsPreferredTeam, selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";

interface TaskBoardDetailBreadcrumbProps {
  title: string;
  taskBoardId: string;
}

const TaskBoardDetailBreadcrumb: React.FC<TaskBoardDetailBreadcrumbProps> = ({ title, taskBoardId }) => {
  const { t } = useTranslation();
  const currentWorkspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);
  const currentTeam = useTypedSelector(selectCurrentAccountsPreferredTeam);

  return currentWorkspace?.isPersonal ? null : (
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
      <BreadcrumbLink
        label={title}
        url={`/${currentWorkspace?.username || ""}/${currentTeam?.username || ""}/task-boards/${taskBoardId}`}
      />
    </Breadcrumb>
  );
};

export default TaskBoardDetailBreadcrumb;
