import useTranslation from "@/locales/useTranslation";
import React from "react";
import styles from "./JinearProInfo.module.css";
import ProPlanFeatureList from "@/components/jinearPricingPlan/jinearProPlan/proPlanFeatureList/ProPlanFeatureList";
import FormLogo from "@/components/form-logo/FormLogo.tsx";

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
        <span>
            <b>{t("pricesPageProFeature_additionalToBasicPlanText")}</b>
          </span>
      )}
      <ProPlanFeatureList contrast={false} />
    </>
  );
};

export default JinearProInfo;
