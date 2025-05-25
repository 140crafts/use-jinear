import React from "react";
import styles from "./JinearSelfHostedInfo.module.css";
import useTranslation from "@/locals/useTranslation";
import FormLogo from "@/components/formLogo/FormLogo";

interface JinearSelfHostedInfoProps {

}

const JinearSelfHostedInfo: React.FC<JinearSelfHostedInfoProps> = ({}) => {
  const { t } = useTranslation();

  return (
    <>
      <div className={styles.headerContainer}>
        <div className={styles.proLogoContainer}>
          <FormLogo withLeftLine={false} />
          <div className={styles.selfHostedLabel}>SELF HOST</div>
        </div>
      </div>

      <span>
            <b>{t("pricesPageSelfHostFeature_additionalToProPlanText")}</b>
      </span>
      <ul className={styles.featureList}>
        <li>{t("pricesPageSelfHostFeature_Own")}</li>
        <li>{t("pricesPageSelfHostFeature_Privacy")}</li>
        <li>{t("pricesPageSelfHostFeature_EasyInstall")}</li>
      </ul>
    </>
  );
};

export default JinearSelfHostedInfo;