import React from "react";
import styles from "./Week.module.css";

interface WeekProps {
  days: Date[];
}

const Week: React.FC<WeekProps> = ({ days }) => {
  return <div className={styles.container}></div>;
};

export default Week;
