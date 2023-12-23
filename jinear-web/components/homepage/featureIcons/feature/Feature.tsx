import useTranslation from "locales/useTranslation";
import React from "react";
import { IFeature } from "../FeatureIcons";
import styles from "./Feature.module.scss";

interface FeatureProps {
  feature: IFeature;
}

const Feature: React.FC<FeatureProps> = ({ feature }) => {
  const { t } = useTranslation();
  const Icon = feature.icon;
  return (
    <div className={styles.container}>
      <Icon className={styles.icon} />
      <div className={styles.label}>{t(feature.label)}</div>
    </div>
  );
};

export default Feature;
