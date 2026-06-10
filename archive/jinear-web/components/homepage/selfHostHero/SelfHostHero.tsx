import React from "react";
import styles from "./SelfHostHero.module.scss";
import useTranslation from "locales/useTranslation";
import { LuHardDrive, LuRefreshCw, LuCloud } from "react-icons/lu";
import MacTerminal from "@/components/homepage/macTerminal/MacTerminal";


const SelfHostHero: React.FC = () => {
  const { t } = useTranslation();

  const features = [
    {
      id: "storage",
      Icon: LuHardDrive,
      title: t("landingPageSelfHostFeature1Title"),
      text: t("landingPageSelfHostFeature1Text"),
    },
    {
      id: "backups",
      Icon: LuRefreshCw,
      title: t("landingPageSelfHostFeature2Title"),
      text: t("landingPageSelfHostFeature2Text"),
    },
    {
      id: "cloud",
      Icon: LuCloud,
      title: t("landingPageSelfHostFeature3Title"),
      text: t("landingPageSelfHostFeature3Text"),
    },
  ];

  return (
    <div className={styles.container}>
      <p className={styles.eyebrow}>{t("landingPageSelfHostEyebrow")}</p>
      <h2 className={styles.title}>
        {t("landingPageSelfHostTitle1")}
        <br />
        {t("landingPageSelfHostTitle2")}
      </h2>
      <p className={styles.text}>{t("landingPageSelfHostText")}</p>

      <div className={styles.terminalBlock}>
        <MacTerminal command="curl -sSL https://gitlab.com/140crafts/use-jinear/-/raw/main/jinear-installation-scripts/install.sh -o install.sh && chmod +x install.sh && ./install.sh" />
      </div>

      <div className={styles.featureGrid}>
        {features.map(({ id, Icon, title, text }) => (
          <div key={id} className={styles.featureCard}>
            <Icon className={styles.featureIcon} />
            <h3 className={styles.featureTitle}>{title}</h3>
            <p className={styles.featureText}>{text}</p>
          </div>
        ))}
      </div>
    </div>
  );
};

export default SelfHostHero;
