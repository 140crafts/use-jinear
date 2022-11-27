import React from "react";
import styles from "./Line.module.css";

interface LineProps {}

const Line: React.FC<LineProps> = ({}) => {
  return <div className={styles.line} />;
};

export default Line;
