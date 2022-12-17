import { useRouter } from "next/router";
import React from "react";
import styles from "./index.module.css";

interface TaskListScreenProps {}

const TaskListScreen: React.FC<TaskListScreenProps> = ({}) => {
  const router = useRouter();
  const workspaceName: string = router.query?.workspaceName as string;
  const teamUsername: string = router.query?.teamUsername as string;

  return (
    <div className={styles.container}>
      TaskListScreen: {workspaceName + teamUsername}
    </div>
  );
};

export default TaskListScreen;
