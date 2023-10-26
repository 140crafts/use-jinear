"use client";
import Logger from "@/utils/logger";
import { useParams, useRouter } from "next/navigation";
import React, { useEffect } from "react";
import styles from "./index.module.css";

interface TeamPageProps {}

const logger = Logger("TeamPage");

const TeamPage: React.FC<TeamPageProps> = ({}) => {
  const router = useRouter();
  const params = useParams();
  const workspaceName: string = params?.workspaceName as string;
  const teamUsername: string = params?.teamUsername as string;

  useEffect(() => {
    router.replace(`/${workspaceName}/tasks/${teamUsername}/tasks`);
  }, []);

  return <div className={styles.container}>{`${workspaceName || ""} > ${teamUsername || ""}`} </div>;
};

export default TeamPage;
