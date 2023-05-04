import PaginatedAssignedToCurrentAccountTaskList from "@/components/taskListScreen/taskLists/paginatedAssignedToCurrentAccountTaskList/PaginatedAssignedToCurrentAccountTaskList";
import { selectCurrentAccountsPreferredWorkspaceId } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import React from "react";
import styles from "./index.module.css";

interface AssignedToMePageProps {}

const AssignedToMePage: React.FC<AssignedToMePageProps> = ({}) => {
  const currentWorkspaceId = useTypedSelector(selectCurrentAccountsPreferredWorkspaceId);
  return (
    <div className={styles.container}>
      {currentWorkspaceId && <PaginatedAssignedToCurrentAccountTaskList workspaceId={currentWorkspaceId} />}
    </div>
  );
};

export default AssignedToMePage;
