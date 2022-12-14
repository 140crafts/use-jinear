import React from "react";
import styles from "./SectionTitle.module.css";

interface SectionTitleProps {
  title: string;
  description: string;
}

const SectionTitle: React.FC<SectionTitleProps> = ({ title, description }) => {
  return (
    <div className={styles.container}>
      <div className={styles.title}>{title}</div>
      <div>{description}</div>
    </div>
  );
};

export default SectionTitle;
