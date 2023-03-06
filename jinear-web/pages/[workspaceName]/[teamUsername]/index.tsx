import Calendar from "@/components/calendar/Calendar";
import { selectCurrentAccountsPreferredWorkspaceId } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { useRouter } from "next/router";
import React from "react";
import styles from "./index.module.css";

interface TeamPageProps {}

const TeamPage: React.FC<TeamPageProps> = ({}) => {
  const router = useRouter();
  const workspaceName: string = router.query?.workspaceName as string;
  const teamUsername: string = router.query?.teamUsername as string;
  const currentWorkspaceId = useTypedSelector(selectCurrentAccountsPreferredWorkspaceId);

  return <div className={styles.container}>{currentWorkspaceId && <Calendar workspaceId={currentWorkspaceId} />}</div>;
};

export default TeamPage;
