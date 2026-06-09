import React from "react";
import styles from "./ComplianceSection.module.scss";
import useTranslation from "locales/useTranslation";
import { LuShieldCheck, LuSearch, LuWifiOff } from "react-icons/lu";

const ComplianceSection: React.FC = () => {
  const { t } = useTranslation();

  const items = [
    {
      id: "gdpr",
      Icon: LuShieldCheck,
      title: t("landingPageCompliance1Title"),
      text: t("landingPageCompliance1Text"),
    },
    {
      id: "audit",
      Icon: LuSearch,
      title: t("landingPageCompliance2Title"),
      text: t("landingPageCompliance2Text"),
    },
    {
      id: "telemetry",
      Icon: LuWifiOff,
      title: t("landingPageCompliance3Title"),
      text: t("landingPageCompliance3Text"),
    },
  ];

  return (
    <div className={styles.container}>
      <p className={styles.eyebrow}>{t("landingPageComplianceEyebrow")}</p>
      <h2 className={styles.title}>{t("landingPageComplianceTitle")}</h2>
      <p className={styles.note}>{t("landingPageComplianceNote")}</p>
      <div className={styles.items}>
        {items.map(({ id, Icon, title, text }) => (
          <div key={id} className={styles.item}>
            <Icon className={styles.icon} />
            <h3 className={styles.itemTitle}>{title}</h3>
            <p className={styles.itemText}>{text}</p>
          </div>
        ))}
      </div>
    </div>
  );
};

export default ComplianceSection;
