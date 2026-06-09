import React from "react";
import styles from "./SocialProofBar.module.scss";
import useTranslation from "locales/useTranslation";

const SocialProofBar: React.FC = () => {
  const { t } = useTranslation();

  const items = [
    t("landingPageProofMit"),
    t("landingPageProofDocker"),
    t("landingPageProofOneCommand"),
    t("landingPageProofSelfHostable"),
    t("landingPageProofOpenSource"),
  ];

  return (
    <div className={styles.container}>
      {items.map((item, index) => (
        <React.Fragment key={index}>
          <span className={styles.item}>{item}</span>
          {index < items.length - 1 && <span className={styles.divider}>·</span>}
        </React.Fragment>
      ))}
    </div>
  );
};

export default SocialProofBar;
