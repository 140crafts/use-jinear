import Logger from "@/utils/logger";
import { useRouter } from "next/router";
import React, { useEffect } from "react";
import styles from "./index.module.css";

interface TeamPageProps {}

const logger = Logger("TeamPage");

const TeamPage: React.FC<TeamPageProps> = ({}) => {
  const router = useRouter();
  const workspaceName: string = router.query?.workspaceName as string;
  const teamUsername: string = router.query?.teamUsername as string;

  useEffect(() => {
    router.replace(`/${workspaceName}/${teamUsername}/tasks`);
  }, []);

  return <div className={styles.container}>{`${workspaceName || ""} > ${teamUsername || ""}`} </div>;
};

export default TeamPage;
