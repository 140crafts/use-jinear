import React from "react";
import styles from "./JinearFreePlan.module.css";
import JinearPricingPlan from "@/components/jinearPricingPlan/JinearPricingPlan";
import Button, { ButtonVariants } from "@/components/button";
import useTranslation from "@/locals/useTranslation";

interface JinearFreePlanProps {

}

const JinearFreePlan: React.FC<JinearFreePlanProps> = ({}) => {
  const { t } = useTranslation();

  return (
    <JinearPricingPlan
      planName={"Starter"}
      price={t("pricesPageBasicPrice")}
      description={t("pricesPageBasicDescription")}
      featuresTitle={t("pricesPageBasicFeature_Features")}
    >
      <ul>
        <li>{t("pricesPageBasicFeature_UnlimitedTasks")}</li>
        <li>{t("pricesPageBasicFeature_UnlimitedLabels")}</li>
        <li>{t("pricesPageBasicFeature_UnlimitedChecklist")}</li>
        <li>{t("pricesPageBasicFeature_UnlimitedBoards")}</li>
        <li>{t("pricesPageBasicFeature_ProjectManagement")}</li>
        <li>{t("pricesPageBasicFeature_Calendar")}</li>
        <li>{t("pricesPageBasicFeature_Reminders")}</li>
        <li>{t("pricesPageBasicFeature_collaborative")}</li>
        <li>{t("pricesPageBasicFeature_GoogleCalendar")}</li>
      </ul>

      <div className={"spacer-h-4"} />
      <div className={"flex-1"} />

      <Button href={"/register"} variant={ButtonVariants.contrast}>
        <span>{t("registerWithMailFormTitle")}</span>
      </Button>

      <div className={"spacer-h-2"} />
    </JinearPricingPlan>

  );
};

export default JinearFreePlan;