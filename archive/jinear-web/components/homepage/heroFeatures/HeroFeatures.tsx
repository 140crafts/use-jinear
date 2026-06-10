import React from "react";
import styles from "./HeroFeatures.module.css";
import HeroFeatureColumn from "@/components/homepage/heroFeatures/heroFeatureColumn/HeroFeatureColumn";
import useTranslation from "@/locals/useTranslation";

const HeroFeatures: React.FC = ({}) => {
  const { t } = useTranslation();

  return (
    <div className={styles.container}>
      <HeroFeatureColumn index={1} title={t("landingPageHeroFeature1Title")} text={t("landingPageHeroFeature1Text")} />
      <HeroFeatureColumn index={2} title={t("landingPageHeroFeature2Title")} text={t("landingPageHeroFeature2Text")} />
      <HeroFeatureColumn index={3} title={t("landingPageHeroFeature3Title")} text={t("landingPageHeroFeature3Text")} />
      <HeroFeatureColumn index={4} title={t("landingPageHeroFeature4Title")} text={t("landingPageHeroFeature4Text")} />
    </div>
  );
};

export default HeroFeatures;
