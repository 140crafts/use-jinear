import TaskListList from "@/components/taskListsListScreen/taskListList/TaskListList";
import TaskListListBreadCrumb from "@/components/taskListsListScreen/taskListListBreadCrumb/TaskListListBreadCrumb";
import { selectCurrentAccountsPreferredTeamId, selectCurrentAccountsPreferredWorkspaceId } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import React from "react";
import styles from "./index.module.css";

interface TaskListsListScreenProps {}

const TaskListsListScreen: React.FC<TaskListsListScreenProps> = ({}) => {
  const teamId = useTypedSelector(selectCurrentAccountsPreferredTeamId);
  const workspaceId = useTypedSelector(selectCurrentAccountsPreferredWorkspaceId);

  return (
    <div className={styles.container}>
      <TaskListListBreadCrumb />
      {teamId && workspaceId && <TaskListList teamId={teamId} workspaceId={workspaceId} />}
    </div>
  );
};

export default TaskListsListScreen;
