import React from "react";
import styles from "./Hero.module.scss";

interface HeroProps {
  title1: string;
  title2?: string;
  text: string;
}

const Hero: React.FC<HeroProps> = ({ title1, title2, text }) => {
  return (
    <div className={styles.heroContainer}>
      <span className={styles.heroTitle}>
        <span className={styles.line}>{title1}</span>
        {title2 && <br />}
        <span className={styles.line}>{title2}</span>
      </span>

      <span className={styles.heroText}>{text}</span>
    </div>
  );
};

export default Hero;
