import React from "react";
import styles from "./HeroFeatureColumn.module.css";
import cn from "classnames";

interface HeroFeatureColumnProps {
  title: string;
  text: string;
  index: number;
  className?: string;
}

const HeroFeatureColumn: React.FC<HeroFeatureColumnProps> = ({ title, text, index, className }) => {
  return (
    <div className={cn(styles.container, className)}>
      <span className={styles.index}>0{index}</span>
      <h3 className={styles.title} dangerouslySetInnerHTML={{ __html: title }} />
      <p className={styles.text}>{text}</p>
    </div>
  );
};

export default HeroFeatureColumn;
