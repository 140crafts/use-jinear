import dynamic from "next/dynamic";
import { useRouter } from "next/router";
import React from "react";
import styles from "./index.module.css";

interface TaskListScreenProps {}

const TextEditor = dynamic(import("@/components/textEditor/TextEditor"), {
  ssr: false,
});

const TaskListScreen: React.FC<TaskListScreenProps> = ({}) => {
  const router = useRouter();
  const workspaceName: string = router.query?.workspaceName as string;
  const teamUsername: string = router.query?.teamUsername as string;

  return (
    <div className={styles.container}>
      TaskListScreen: {workspaceName + teamUsername}
      <TextEditor variant="full" />
      <div>123</div>
    </div>
  );
};

export default TaskListScreen;
