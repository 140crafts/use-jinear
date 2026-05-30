import React from "react";
import styles from "./ProPlanFeatureList.module.css";
import { IoInformationCircleOutline } from "react-icons/io5";
import useTranslation from "@/locals/useTranslation";

interface ProPlanFeatureListProps {
  contrast?: boolean;
}

const ProPlanFeatureList: React.FC<ProPlanFeatureListProps> = ({ contrast = true }) => {
  const { t } = useTranslation();

  return (
    <ul className={styles.list}>
      <li>{t("pricesPageProFeature_collaborative")}</li>
      <li><b>{t("pricesPageProFeature_fixedPrices")}</b></li>
      <li
        data-tooltip-multiline={!contrast ? t("pricesPageProFeature_team_task_visibility_description") : undefined}
        data-tooltip-multiline-contrast={contrast ? t("pricesPageProFeature_team_task_visibility_description") : undefined}
      >
        <div>
          <span>{t("pricesPageProFeature_team_task_visibility")}</span>
          <IoInformationCircleOutline className={styles.infoIcon} />
        </div>
      </li>
      <li>
        {t("pricesPageProFeature_ProjectBoards")}&nbsp;
        <span dangerouslySetInnerHTML={{ __html: t("pricesPageProFeature_ProjectBoards_Example") }}></span>
      </li>
      <li>{t("pricesPageProFeature_file")}</li>
      <li>{t("pricesPageProFeature_unlimitedFileStorage")}</li>
      <li>{t("pricesPageProFeature_UnlimitedSupport")}</li>
    </ul>
  );
};

export default ProPlanFeatureList;