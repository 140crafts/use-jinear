import Calendar from "@/components/calendar/Calendar";
import { selectWorkspaceFromWorkspaceUsername } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { useRouter } from "next/router";
import React from "react";
import styles from "./index.module.css";

interface WorkspacePageProps {}

const WorkspacePage: React.FC<WorkspacePageProps> = ({}) => {
  const router = useRouter();
  const workspaceName: string = router.query?.workspaceName as string;
  const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));

  return <div className={styles.container}>{workspace && <Calendar className={styles.calendar} workspace={workspace} />}</div>;
};
export default WorkspacePage;
