import TaskListFilterBar from "@/components/taskLists/taskListFilterBar/TaskListFilterBar";
import { selectCurrentAccountsPreferredTeamId, selectCurrentAccountsPreferredWorkspaceId } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import React from "react";
import styles from "./index.module.css";

interface TestPageProps {}

const TestPage: React.FC<TestPageProps> = ({}) => {
  const workspaceId = useTypedSelector(selectCurrentAccountsPreferredWorkspaceId);
  const teamId = useTypedSelector(selectCurrentAccountsPreferredTeamId);
  return (
    <div className={styles.container}>
      {workspaceId && teamId && <TaskListFilterBar workspaceId={workspaceId} teamId={teamId} />}
    </div>
  );
};

export default TestPage;
