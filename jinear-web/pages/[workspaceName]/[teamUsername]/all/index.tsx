import TaskListScreenBreadcrumb from "@/components/taskListScreen/breadcrumb/TaskListScreenBreadcrumb";
import MultiViewTaskList from "@/components/taskLists/multiViewTaskList/MultiViewTaskList";
import { selectCurrentAccountsPreferredTeam, selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./index.module.css";

interface AllTaskListScreenProps {}

const AllTaskListScreen: React.FC<AllTaskListScreenProps> = ({}) => {
  const { t } = useTranslation();
  const workspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);
  const team = useTypedSelector(selectCurrentAccountsPreferredTeam);

  return (
    <div className={styles.container}>
      {!workspace?.isPersonal && <TaskListScreenBreadcrumb type="all" />}
      {team && (
        <MultiViewTaskList
          title={t("allTasksPageListTitle")}
          workspaceId={team.workspaceId}
          teamId={team.teamId}
          activeDisplayFormat="LIST"
        />
      )}
    </div>
  );
};

export default AllTaskListScreen;
