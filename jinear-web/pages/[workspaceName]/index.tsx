import Calendar from "@/components/calendar/Calendar";
import { selectCurrentAccountsPreferredWorkspaceId } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import React from "react";
import styles from "./index.module.css";

interface WorkspacePageProps {}

const WorkspacePage: React.FC<WorkspacePageProps> = ({}) => {
  const currentWorkspaceId = useTypedSelector(selectCurrentAccountsPreferredWorkspaceId);

  return (
    <div className={styles.container}>
      {currentWorkspaceId && <Calendar className={styles.calendar} workspaceId={currentWorkspaceId} />}
    </div>
  );
};
export default WorkspacePage;
