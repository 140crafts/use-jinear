import React from "react";
import styles from "./ComparisonSection.module.scss";
import useTranslation from "locales/useTranslation";

const ComparisonSection: React.FC = () => {
  const { t } = useTranslation();

  const rows = [
    { label: t("landingPageComparisonRow1Label"), cloud: t("landingPageComparisonRow1Cloud"), selfHosted: t("landingPageComparisonRow1Self") },
    { label: t("landingPageComparisonRow2Label"), cloud: t("landingPageComparisonRow2Cloud"), selfHosted: t("landingPageComparisonRow2Self") },
    { label: t("landingPageComparisonRow3Label"), cloud: t("landingPageComparisonRow3Cloud"), selfHosted: t("landingPageComparisonRow3Self") },
    { label: t("landingPageComparisonRow4Label"), cloud: t("landingPageComparisonRow4Cloud"), selfHosted: t("landingPageComparisonRow4Self") },
    { label: t("landingPageComparisonRow5Label"), cloud: t("landingPageComparisonRow5Cloud"), selfHosted: t("landingPageComparisonRow5Self") },
    { label: t("landingPageComparisonRow6Label"), cloud: t("landingPageComparisonRow6Cloud"), selfHosted: t("landingPageComparisonRow6Self") },
  ];

  return (
    <div className={styles.container}>
      <h2 className={styles.title}>{t("landingPageComparisonTitle")}</h2>
      <p className={styles.subtitle}>{t("landingPageComparisonSubtitle")}</p>
      <div className={styles.tableWrapper}>
        <table className={styles.table}>
          <thead>
            <tr>
              <th className={styles.th} />
              <th className={styles.th}>{t("landingPageComparisonCloud")}</th>
              <th className={styles.th}>{t("landingPageComparisonSelfHosted")}</th>
            </tr>
          </thead>
          <tbody>
            {rows.map((row, index) => (
              <tr key={index} className={index % 2 === 0 ? styles.rowEven : styles.rowOdd}>
                <td className={styles.tdLabel}>{row.label}</td>
                <td className={styles.td}>{row.cloud}</td>
                <td className={styles.tdHighlight}>{row.selfHosted}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <p className={styles.note}>{t("landingPageComparisonNote")}</p>
    </div>
  );
};

export default ComparisonSection;
