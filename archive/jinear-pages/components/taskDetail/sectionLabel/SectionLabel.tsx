import React from "react";
import styles from "./SectionLabel.module.css";

interface SectionLabelProps {
  label: string;
}

const SectionLabel: React.FC<SectionLabelProps> = ({ label }) => {
  return <div className={styles.label}>{label}</div>;
};

export default SectionLabel;
