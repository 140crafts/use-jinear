import React from "react";
import styles from "./SectionTitle.module.css";

interface SectionTitleProps {
  title: string;
  description: string;
}

const SectionTitle: React.FC<SectionTitleProps> = ({ title, description }) => {
  return (
    <div className={styles.container}>
      <h2 className={styles.title}>{title}</h2>
      <div>{description}</div>
    </div>
  );
};

export default SectionTitle;
