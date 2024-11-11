import React from "react";
import styles from "./HeroFeatures.module.css";
import HeroFeatureColumn from "@/components/homepage/heroFeatures/heroFeatureColumn/HeroFeatureColumn";
import { useTheme } from "@/components/themeProvider/ThemeProvider";
import useTranslation from "@/locals/useTranslation";

interface HeroFeaturesProps {

}

const HeroFeatures: React.FC<HeroFeaturesProps> = ({}) => {
  const { t } = useTranslation();
  const theme = useTheme();
  const srcType = theme == "light" ? "light" : "dark";

  return (
    <div className={styles.container}>
      <HeroFeatureColumn
        title={t("landingPageHeroFeature1Title")}
        text={t("landingPageHeroFeature1Text")}
        imgSrc={`https://storage.googleapis.com/jinear-b0/web-assets/jinear-homescreen-images/v2/task-zoomed-${srcType}.png`}
      />
      <HeroFeatureColumn
        title={t("landingPageHeroFeature2Title")}
        text={t("landingPageHeroFeature2Text")}
        imgSrc={`https://storage.googleapis.com/jinear-b0/web-assets/jinear-homescreen-images/v2/task-list-zoomed-${srcType}.png`}
      />
      <HeroFeatureColumn
        title={t('landingPageHeroFeature3Title')}
        text={t('landingPageHeroFeature3Text')}
        imgSrc={`https://storage.googleapis.com/jinear-b0/web-assets/jinear-homescreen-images/v2/cal-zoomed-${srcType}.png`}
      />
      <HeroFeatureColumn
        title={t('landingPageHeroFeature4Title')}
        text={t("landingPageHeroFeature4Text")}
        imgSrc={`https://storage.googleapis.com/jinear-b0/web-assets/jinear-homescreen-images/v2/home-overview-${srcType}.png`}
      />
    </div>
  );
};

export default HeroFeatures;