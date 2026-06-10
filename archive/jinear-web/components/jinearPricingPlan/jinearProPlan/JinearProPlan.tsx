import React from "react";
import JinearPricingPlan from "@/components/jinearPricingPlan/JinearPricingPlan";
import { PADDLE_CATALOG } from "@/utils/constants";
import useTranslation from "@/locals/useTranslation";
import ProPlanFeatureList from "@/components/jinearPricingPlan/jinearProPlan/proPlanFeatureList/ProPlanFeatureList";

interface JinearProPlanProps {
  actionButton?: React.ReactNode;
  contrast?: boolean;
}

const JinearProPlan: React.FC<JinearProPlanProps> = ({ actionButton, contrast = true }) => {
  const { t } = useTranslation();

  return (
    <JinearPricingPlan
      planName={"Smarter"}
      price={
        <mark>
          {t("upgradeWorkspaceTierButtonMonthly").replace("${price}", PADDLE_CATALOG.business_monthly.price)}
        </mark>}
      description={t("pricesPageProDescription")}
      featuresTitle={t("pricesPageProFeature_AnythingFromFree")}
      contrast={contrast}
    >
      <ProPlanFeatureList contrast={contrast} />

      {actionButton}
    </JinearPricingPlan>
  );
};

export default JinearProPlan;