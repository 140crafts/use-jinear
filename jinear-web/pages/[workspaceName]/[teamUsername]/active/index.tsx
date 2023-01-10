import TaskListGroupedByWorkflowStatus from "@/components/taskListScreen/breadcrumb/taskListGroupedByWorkflowStatus/TaskListGroupedByWorkflowStatus";
import TaskListScreenBreadcrumb from "@/components/taskListScreen/breadcrumb/TaskListScreenBreadcrumb";
import { selectCurrentAccountsPreferredTeam } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { useRouter } from "next/router";
import React from "react";
import styles from "./index.module.css";

interface ActiveTaskListScreenProps {}

const ActiveTaskListScreen: React.FC<ActiveTaskListScreenProps> = ({}) => {
  const router = useRouter();
  const team = useTypedSelector(selectCurrentAccountsPreferredTeam);

  return (
    <div className={styles.container}>
      <TaskListScreenBreadcrumb type="active" />
      {team && (
        <TaskListGroupedByWorkflowStatus
          type="active"
          teamId={team.teamId}
          workspaceId={team.workspaceId}
        />
      )}
    </div>
  );
};

export default ActiveTaskListScreen;
