import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./OrLine.module.css";

interface OrLineProps {
  text?: string;
  omitText?: boolean;
}

const OrLine: React.FC<OrLineProps> = ({ text, omitText = false }) => {
  const { t } = useTranslation();
  return (
    <div className={styles.orDividerContainer}>
      <div className={styles.orLineLeft} />
      {!omitText && <div className={styles.orLabel}>{text ? text : t("or")}</div>}
      <div className={styles.orLineRight} />
    </div>
  );
};

export default OrLine;
