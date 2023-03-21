import TaskListScreenBreadcrumb from "@/components/taskListScreen/breadcrumb/TaskListScreenBreadcrumb";
import PaginatedAllTasksList from "@/components/taskListScreen/taskLists/paginatedAllTasksList/PaginatedAllTasksList";
import { selectCurrentAccountsPersonalWorkspace, selectCurrentAccountsPreferredTeam } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import { useRouter } from "next/router";
import React from "react";
import styles from "./index.module.css";

interface AllTaskListScreenProps {}

const AllTaskListScreen: React.FC<AllTaskListScreenProps> = ({}) => {
  const { t } = useTranslation();
  const router = useRouter();
  const workspace = useTypedSelector(selectCurrentAccountsPersonalWorkspace);
  const team = useTypedSelector(selectCurrentAccountsPreferredTeam);

  return (
    <div className={styles.container}>
      {!workspace?.isPersonal && <TaskListScreenBreadcrumb type="all" />}
      {team && <PaginatedAllTasksList workspaceId={team.workspaceId} teamId={team.teamId} name={t("allTasksPageListTitle")} />}
    </div>
  );
};

export default AllTaskListScreen;
