import MiniMonthCalendar from "@/components/miniMonthCalendar/MiniMonthCalendar";
import React from "react";
import styles from "./index.module.css";

interface LastActivitiesScreenProps {}

const LastActivitiesScreen: React.FC<LastActivitiesScreenProps> = ({}) => {
  return (
    <div className={styles.container}>
      <MiniMonthCalendar />
    </div>
  );
};

export default LastActivitiesScreen;
