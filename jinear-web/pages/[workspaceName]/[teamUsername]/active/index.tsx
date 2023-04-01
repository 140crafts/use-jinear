import TaskListScreenBreadcrumb from "@/components/taskListScreen/breadcrumb/TaskListScreenBreadcrumb";
import TaskListGroupedByWorkflowStatus from "@/components/taskListScreen/taskLists/taskListGroupedByWorkflowStatus/TaskListGroupedByWorkflowStatus";
import { selectCurrentAccountsPreferredTeam, selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { useRouter } from "next/router";
import React from "react";
import styles from "./index.module.css";

interface ActiveTaskListScreenProps {}

const ActiveTaskListScreen: React.FC<ActiveTaskListScreenProps> = ({}) => {
  const router = useRouter();
  const team = useTypedSelector(selectCurrentAccountsPreferredTeam);
  const workspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);

  return (
    <div className={styles.container}>
      {!workspace?.isPersonal && <TaskListScreenBreadcrumb type="active" />}
      {team && <TaskListGroupedByWorkflowStatus type="active" teamId={team.teamId} workspaceId={team.workspaceId} />}
    </div>
  );
};

export default ActiveTaskListScreen;
