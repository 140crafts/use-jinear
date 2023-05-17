import Calendar from "@/components/calendar/Calendar";
import { selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import React from "react";
import styles from "./index.module.css";

interface WorkspacePageProps {}

const WorkspacePage: React.FC<WorkspacePageProps> = ({}) => {
  const currentWorkspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);

  return (
    <div className={styles.container}>
      {currentWorkspace && <Calendar className={styles.calendar} workspace={currentWorkspace} />}
    </div>
  );
};
export default WorkspacePage;
