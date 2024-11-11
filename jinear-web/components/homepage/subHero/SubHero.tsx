import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./SubHero.module.scss";
import cn from "classnames";
import Image from "next/image";

interface SubHeroProps {
  title1: string;
  title2?: string;
  text: string;
  className?: string;
}

const SubHero: React.FC<SubHeroProps> = ({ className, title1, title2, text }) => {
  const { t } = useTranslation();

  return (
    <div className={cn(styles.heroContainer, className)}>
      <span className={styles.heroTitle}>
        <span className={styles.line} dangerouslySetInnerHTML={{ __html: title1 }}></span>
        {title2 && <br />}
        {title2 && <span className={styles.line} dangerouslySetInnerHTML={{ __html: title2 }}></span>}
      </span>

      <span className={styles.heroText} dangerouslySetInnerHTML={{ __html: text }}></span>

    </div>
  );
};

export default SubHero;
