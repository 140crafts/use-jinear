import TaskListListBreadCrumb from "@/components/taskBoardsListScreen/taskBoardListBreadCrumb/TaskBoardListBreadCrumb";
import TaskListList from "@/components/taskLists/taskBoardList/TaskBoardList";
import { selectCurrentAccountsPreferredTeam, selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import React from "react";
import styles from "./index.module.css";

interface TaskBoardListScreenProps {}

const TaskBoardListScreen: React.FC<TaskBoardListScreenProps> = ({}) => {
  const team = useTypedSelector(selectCurrentAccountsPreferredTeam);
  const workspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);

  return (
    <div className={styles.container}>
      <TaskListListBreadCrumb />
      {team && workspace && <TaskListList team={team} workspace={workspace} />}
    </div>
  );
};

export default TaskBoardListScreen;
