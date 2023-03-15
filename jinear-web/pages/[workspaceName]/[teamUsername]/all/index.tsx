import TaskListScreenBreadcrumb from "@/components/taskListScreen/breadcrumb/TaskListScreenBreadcrumb";
import PaginatedAllTasksList from "@/components/taskListScreen/taskLists/paginatedAllTasksList/PaginatedAllTasksList";
import { selectCurrentAccountsPreferredTeam } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import { useRouter } from "next/router";
import React from "react";
import styles from "./index.module.css";

interface AllTaskListScreenProps {}

const AllTaskListScreen: React.FC<AllTaskListScreenProps> = ({}) => {
  const { t } = useTranslation();
  const router = useRouter();
  const team = useTypedSelector(selectCurrentAccountsPreferredTeam);

  return (
    <div className={styles.container}>
      <TaskListScreenBreadcrumb type="all" />
      {team && <PaginatedAllTasksList workspaceId={team.workspaceId} teamId={team.teamId} name={t("allTasksPageListTitle")} />}
    </div>
  );
};

export default AllTaskListScreen;
