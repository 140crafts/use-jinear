"use client";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import JinearFreeInfo from "@/components/jinearFreeInfo/JinearFreeInfo";
import JinearProInfo from "@/components/jinearProInfo/JinearProInfo";
import { PADDLE_CATALOG } from "@/components/modal/upgradeWorkspaceModal/UpgradeWorkspaceModal";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./index.module.scss";
import JinearSelfHostedInfo from "@/components/jinearSelfHostedInfo/JinearSelfHostedInfo";
import { closeBasicTextInputModal, popBasicTextInputModal } from "@/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import ThemeToggle from "@/components/themeToggle/ThemeToggle";
import { IoArrowBack, IoArrowForward, IoCaretForward } from "react-icons/io5";
import { trackWaitlist } from "@/utils/tracker";
import { selectCurrentAccountId } from "@/slice/accountSlice";
import posthog from "posthog-js";
import cn from "classnames";

interface PricingPageProps {
}

const PricingPage: React.FC<PricingPageProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const currentAccountId = useTypedSelector(selectCurrentAccountId);

  // const onSelfHostClick = () => {
  //   trackWaitlist({ message: `Self host clicked. currentAccountId: ${currentAccountId}` });
  //   posthog.capture("self-host-click", { currentAccountId });
  //   dispatch(
  //     popBasicTextInputModal({
  //       visible: true,
  //       title: t("selfHostWaitlistModalTitle"),
  //       infoText: t("selfHostWaitlistModalInfoText"),
  //       onSubmit: onEmailSubmit
  //     })
  //   );
  // };

  const onEmailSubmit = (email: string) => {
    dispatch(closeBasicTextInputModal());
    trackWaitlist({ message: `Added to wait-list. email: ${email}` });
    posthog.capture("self-host-waitlist", { email, currentAccountId });
  };

  return (
    <div className={styles.container}>
      <div className={styles.pageTitleContainer}>
        <div className={styles.actionContainer}>
          <Button href="/" variant={ButtonVariants.filled} heightVariant={ButtonHeight.short}>
            <b><IoArrowBack /></b>
          </Button>
        </div>
        <h1>{t("pricingPageTitle")}</h1>
        <div className={styles.themeContainer}><ThemeToggle /></div>
      </div>
      <div className={styles.plansContainer}>

        <div className={styles.plan}>
          <JinearFreeInfo />
          <div className="flex-1" />
          <div className={styles.priceLabel}>{t("pricePageBasicPlan")}</div>
        </div>

          <div className={styles.plan}>
              <JinearSelfHostedInfo />
              <div className={styles.subtextContainer}>
           <span className={styles.subtext}>
            <b>{t("pricesPageSelfHostFeature_RequiresExpertise")}</b>
          </span>
              </div>
              <Button className={styles.priceLabel} variant={ButtonVariants.filled} href={'https://gitlab.com/140crafts/use-jinear'} target={'_blank' }>
                  {t("pricesPageSelfHostFeature_GoToRepo")}
              </Button>
          </div>

        <div className={styles.plan}>
          <JinearProInfo hasAdditionalToBasicPlanText={true} />
          <div className={styles.subtextContainer}>
            <span className={styles.subtext}>
            <b>{t("pricesPageProFeature_subscriptionsAppliesOnlySingleWorkspaceText")}</b>
          </span>
            <span className={styles.subtext}>
            <b>{t("pricesPageProFeature_singleFileSizeLimit")}</b>
          </span>
          </div>
          <div className={styles.priceLabel}>
            {t("upgradeWorkspaceTierButtonMonthly").replace("${price}", PADDLE_CATALOG.business_monthly.price)}
          </div>
          <div className={styles.priceLabel}>
            {t("upgradeWorkspaceTierButtonYearly").replace("${price}", PADDLE_CATALOG.business_yearly.price)}
          </div>

          {/*<Button className={styles.freeText} href={"/register"}>*/}
          {/*  <span>*/}
          {/*    Free Forever<br />*/}
          {/*    If You Signup Today*/}
          {/*  </span>*/}
          {/*  <IoCaretForward size={42} className={"icon"} />*/}
          {/*</Button>*/}
        </div>

      </div>
    </div>
  );
};

export default PricingPage;
