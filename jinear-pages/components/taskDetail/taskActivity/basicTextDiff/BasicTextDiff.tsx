import React from "react";
import styles from "./BasicTextDiff.module.css";

interface BasicTextDiffProps {
  oldState?: React.ReactNode;
  newState?: React.ReactNode;
}

const BasicTextDiff: React.FC<BasicTextDiffProps> = ({ oldState, newState }) => {
  return (
    <div className={styles.container}>
      <div className={styles.cell}>{oldState}</div>
      {newState && <div className={styles.arrow}>{"->"}</div>}
      {newState && <div className={styles.cell}>{newState}</div>}
    </div>
  );
};

export default BasicTextDiff;
