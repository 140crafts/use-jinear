import { useFeatureFlag } from "@/hooks/useFeatureFlag";
import { TranslationKey } from "locales/useTranslation";
import React from "react";
import { IconType } from "react-icons";
import { LuAlarmClock, LuCalendar, LuCheckCircle, LuFile, LuMail, LuUsers2 } from "react-icons/lu";
import styles from "./FeatureIcons.module.scss";
import Feature from "./feature/Feature";

interface FeatureIconsProps {}

export interface IFeature {
  icon: IconType;
  label: TranslationKey;
}

const FEATURES: IFeature[] = [
  {
    icon: LuCalendar,
    label: "homePageFeature_calendar",
  },
  {
    icon: LuAlarmClock,
    label: "homePageFeature_reminders",
  },
  {
    icon: LuUsers2,
    label: "homePageFeature_teams",
  },
  {
    icon: LuMail,
    label: "homePageFeature_gmail",
  },
  {
    icon: LuFile,
    label: "homePageFeature_files",
  },
  {
    icon: LuCheckCircle,
    label: "homePageFeature_workflowmanagement",
  },
];

const FeatureIcons: React.FC<FeatureIconsProps> = ({}) => {
  const feedsEnabled = useFeatureFlag("FEEDS");

  return (
    <div className={styles.container}>
      {FEATURES.filter((feature) => (feature.label == "homePageFeature_gmail" ? feedsEnabled : true)).map((feature) => (
        <Feature key={feature.label} feature={feature} />
      ))}
    </div>
  );
};

export default FeatureIcons;
