import { useRouter } from "next/router";
import React from "react";
import styles from "./index.module.css";

interface TeamPageProps {}

const TeamPage: React.FC<TeamPageProps> = ({}) => {
  const router = useRouter();
  const workspaceName: string = router.query?.workspaceName as string;
  const teamUsername: string = router.query?.teamUsername as string;

  return <div className={styles.container}>{workspaceName + teamUsername}</div>;
};

export default TeamPage;
