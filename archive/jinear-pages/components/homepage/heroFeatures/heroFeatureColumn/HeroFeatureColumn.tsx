import React from "react";
import styles from "./HeroFeatureColumn.module.css";
import cn from "classnames";

interface HeroFeatureColumnProps {
  title: string;
  text: string;
  className?: string;
  imgSrc: string;
}

const HeroFeatureColumn: React.FC<HeroFeatureColumnProps> = ({ title, text, imgSrc, className }) => {

  return (
    <div className={cn(styles.container, className)}>
      <h2 className={styles.title} dangerouslySetInnerHTML={{ __html: title }}>
      </h2>
      <span className={styles.text}>
        {text}
      </span>
      <img src={imgSrc} className={styles.image} />
    </div>
  );
};

export default HeroFeatureColumn;