import TaskListScreenBreadcrumb from "@/components/taskListScreen/breadcrumb/TaskListScreenBreadcrumb";
import TaskListGroupedByWorkflowStatus from "@/components/taskLists/taskListGroupedByWorkflowStatus/TaskListGroupedByWorkflowStatus";
import { selectCurrentAccountsPreferredTeam, selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import React from "react";
import styles from "./index.module.css";

interface TeamArchiveScreenProps {}

const TeamArchiveScreen: React.FC<TeamArchiveScreenProps> = ({}) => {
  const team = useTypedSelector(selectCurrentAccountsPreferredTeam);
  const workspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);

  return (
    <div className={styles.container}>
      {!workspace?.isPersonal && <TaskListScreenBreadcrumb type="archive" />}
      {team && <TaskListGroupedByWorkflowStatus type="archive" teamId={team.teamId} workspaceId={team.workspaceId} />}
    </div>
  );
};

export default TeamArchiveScreen;
