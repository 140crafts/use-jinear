import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./OrLine.module.css";

interface OrLineProps {}

const OrLine: React.FC<OrLineProps> = ({}) => {
  const { t } = useTranslation();
  return (
    <div className={styles.orDividerContainer}>
      <div className={styles.orLineLeft} />
      <div className={styles.orLabel}>{t("or")}</div>
      <div className={styles.orLineRight} />
    </div>
  );
};

export default OrLine;
