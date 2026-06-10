import useTranslation from "locales/useTranslation";
import React from "react";
import FormLogo from "../formLogo/FormLogo";
import styles from "./JinearFreeInfo.module.css";

interface JinearFreeInfoProps {}

const JinearFreeInfo: React.FC<JinearFreeInfoProps> = ({}) => {
  const { t } = useTranslation();

  return (
    <>
      <div className={styles.headerContainer}>
        <div className={styles.proLogoContainer}>
          <FormLogo withLeftLine={false} />
          <div className={styles.freeLabel}>Basic</div>
        </div>
      </div>
      <ul className={styles.featureList}>
        <li>{t("pricesPageBasicFeature_UnlimitedTasks")}</li>
        <li>{t("pricesPageBasicFeature_Reminders")}</li>
        <li>{t("pricesPageBasicFeature_collaborative")}</li>
      </ul>
    </>
  );
};

export default JinearFreeInfo;
