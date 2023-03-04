import TaskListGroupedByWorkflowStatus from "@/components/taskListScreen/breadcrumb/taskListGroupedByWorkflowStatus/TaskListGroupedByWorkflowStatus";
import TaskListScreenBreadcrumb from "@/components/taskListScreen/breadcrumb/TaskListScreenBreadcrumb";
import { selectCurrentAccountsPreferredTeam } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import React from "react";
import styles from "./index.module.css";

interface TeamArchiveScreenProps {}

const TeamArchiveScreen: React.FC<TeamArchiveScreenProps> = ({}) => {
  const team = useTypedSelector(selectCurrentAccountsPreferredTeam);

  return (
    <div className={styles.container}>
      <TaskListScreenBreadcrumb type="archive" />
      {team && <TaskListGroupedByWorkflowStatus type="archive" teamId={team.teamId} workspaceId={team.workspaceId} />}
    </div>
  );
};

export default TeamArchiveScreen;
