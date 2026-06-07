import useTranslation from "locales/useTranslation";
import React from "react";
import { IoInformationCircleOutline } from "react-icons/io5";
import FormLogo from "../formLogo/FormLogo";
import styles from "./JinearProInfo.module.css";

interface JinearProInfoProps {
  hasAdditionalToBasicPlanText?: boolean;
}

const JinearProInfo: React.FC<JinearProInfoProps> = ({ hasAdditionalToBasicPlanText = false }) => {
  const { t } = useTranslation();

  return (
    <>
      <div className={styles.headerContainer}>
        <div className={styles.proLogoContainer}>
          <FormLogo withLeftLine={false} />
          <div className={styles.proLabel}>PRO</div>
        </div>
      </div>
      {hasAdditionalToBasicPlanText && (
        <>
          <span>
            <b>{t("pricesPageProFeature_additionalToBasicPlanText")}</b>
          </span>
        </>
      )}
      <ul className={styles.featureList}>
        <li>{t("pricesPageProFeature_collaborative")}</li>
        <li>{t("pricesPageProFeature_fixedPrices")}</li>
        <li data-tooltip-multiline={t("pricesPageProFeature_team_task_visibility_description")}>
          <div className={styles.featureListItemContainer}>
            {t("pricesPageProFeature_team_task_visibility")}
            <IoInformationCircleOutline />
          </div>
        </li>
        <li>{t("pricesPageProFeature_file")}</li>
        <li>{t("pricesPageProFeature_unlimitedFileStorage")}</li>
        <li>{t("pricesPageProFeature_UnlimitedSupport")}</li>
        {/*<li>{t("pricesPageProFeature_1to1Onboarding")}</li>*/}
      </ul>
    </>
  );
};

export default JinearProInfo;
