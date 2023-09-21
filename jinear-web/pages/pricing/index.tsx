import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import JinearFreeInfo from "@/components/jinearFreeInfo/JinearFreeInfo";
import JinearProInfo from "@/components/jinearProInfo/JinearProInfo";
import { PADDLE_CATALOG } from "@/components/modal/upgradeWorkspaceModal/UpgradeWorkspaceModal";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./index.module.scss";

interface PricingPageProps {}

const PricingPage: React.FC<PricingPageProps> = ({}) => {
  const { t } = useTranslation();

  return (
    <div className={styles.container}>
      <div className={styles.pageTitleContainer}>
        <div className={styles.actionContainer}>
          <Button href="/" variant={ButtonVariants.filled} heightVariant={ButtonHeight.short}>
            {/* {t("pricingPageGoBack")} */}
            <b>{"<-"}</b>
          </Button>
        </div>
        <h1>{t("pricingPageTitle")}</h1>
      </div>
      <div className={styles.plansContainer}>
        <div className={styles.plan}>
          <JinearFreeInfo />
          <div className="flex-1" />
          <div className={styles.priceLabel}>{t("pricePageBasicPlan")}</div>
        </div>

        <div className={styles.plan}>
          <JinearProInfo hasAdditionalToBasicPlanText={true} />
          <div className="spacer-h-2" />
          <span className={styles.subtext}>
            <b>{t("pricesPageProFeature_subscriptionsAppliesOnlySingleWorkspaceText")}</b>
          </span>
          <span className={styles.subtext}>
            <b>{t("pricesPageProFeature_singleFileSizeLimit")}</b>
          </span>
          <div className="flex-1" />
          <div className={styles.priceLabel}>
            {t("upgradeWorkspaceTierButtonMonthly").replace("${price}", PADDLE_CATALOG.business_monthly.price)}
          </div>
          <div className={styles.priceLabel}>
            {t("upgradeWorkspaceTierButtonYearly").replace("${price}", PADDLE_CATALOG.business_yearly.price)}
          </div>
        </div>
      </div>
    </div>
  );
};

export default PricingPage;
