import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./Hero.module.scss";

interface HeroProps {}

const Hero: React.FC<HeroProps> = ({}) => {
  const { t } = useTranslation();

  return (
    <div className={styles.heroContainer}>
      <span className={styles.heroTitle}>
        <span className={styles.heroHighlighted}>{t("homescreenHeroTitleLine1")}</span>
        <br />
        {t("homescreenHeroTitleLine2")}
      </span>

      <span className={styles.heroText}>{t("homescreenHeroText")}</span>
    </div>
  );
};

export default Hero;
