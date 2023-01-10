import TaskListGroupedByWorkflowStatus from "@/components/taskListScreen/breadcrumb/taskListGroupedByWorkflowStatus/TaskListGroupedByWorkflowStatus";
import TaskListScreenBreadcrumb from "@/components/taskListScreen/breadcrumb/TaskListScreenBreadcrumb";
import { selectCurrentAccountsPreferredTeam } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import React from "react";
import styles from "./index.module.css";

interface BacklogTaskListScreenProps {}

const BacklogTaskListScreen: React.FC<BacklogTaskListScreenProps> = ({}) => {
  const team = useTypedSelector(selectCurrentAccountsPreferredTeam);

  return (
    <div className={styles.container}>
      <TaskListScreenBreadcrumb type="backlog" />
      {team && (
        <TaskListGroupedByWorkflowStatus
          type="backlog"
          teamId={team.teamId}
          workspaceId={team.workspaceId}
        />
      )}
    </div>
  );
};

export default BacklogTaskListScreen;
