import NotificationInfo from "@/components/notificationInfo/NotificationInfo";
import React from "react";
import styles from "./index.module.css";

interface DebugScreenProps {}

const DebugScreen: React.FC<DebugScreenProps> = ({}) => {
  return (
    <div className={styles.container}>
      <NotificationInfo />
    </div>
  );
};

export default DebugScreen;
