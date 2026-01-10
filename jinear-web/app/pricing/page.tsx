"use client";
import Button, { ButtonVariants } from "@/components/button";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./index.module.scss";
import JinearPricingPlan from "@/components/jinearPricingPlan/JinearPricingPlan";
import JinearFreePlan from "@/components/jinearPricingPlan/jinearFreePlan/JinearFreePlan";
import JinearProPlan from "@/components/jinearPricingPlan/jinearProPlan/JinearProPlan";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import { selectAuthState } from "@/slice/accountSlice";
import { useAccountsPreferredWorkspaceIfLoggedIn } from "@/hooks/useAccountsPreferredWorkspaceIfLoggedIn";
import { popUpgradeWorkspacePlanModal } from "@/slice/modalSlice";
import { useRouter } from "next/navigation";
import { FaGithub, FaGitlab } from "react-icons/fa6";
import HomePageNavbar from "@/components/homepage/navbar/HomePageNavbar";
import Hero from "@/components/homepage/hero/Hero";
import Footer from "@/components/homepage/footer/Footer";
import FormLogo from "@/components/formLogo/FormLogo";
import { SiAsana, SiDropbox, SiGoogle, SiSlack } from "react-icons/si";
import cn from "classnames";
import { LuEqual, LuPlus } from "react-icons/lu";
import OrLine from "@/components/orLine/OrLine";

interface PricingPageProps {
}

const PricingPage: React.FC<PricingPageProps> = ({}) => {
  const { t } = useTranslation();
  const authState = useTypedSelector(selectAuthState);
  const preferredWorkspace = useAccountsPreferredWorkspaceIfLoggedIn();
  const tier = preferredWorkspace?.tier;
  const preferredRedirectRoute = preferredWorkspace ? `/${preferredWorkspace?.username}` : undefined;

  const dispatch = useAppDispatch();
  const router = useRouter();

  const onProPlanCtaClick = (event: React.MouseEvent<HTMLAnchorElement> | undefined) => {
    if (event && authState == "LOGGED_IN" && preferredWorkspace && preferredRedirectRoute) {
      event?.preventDefault();
      tier == "BASIC" && dispatch(popUpgradeWorkspacePlanModal({
        workspaceId: preferredWorkspace.workspaceId,
        visible: true
      }));
      setTimeout(() => {
        router.push(preferredRedirectRoute);
      }, 1000);
    }
  };

  return (
    <div className={styles.container}>
      <HomePageNavbar />

      <div className="spacer-h-12" />
      <Hero
        title1={t("pricingScreenHeroTitleLine1")}
        title2={t("pricingScreenHeroTitleLine2")}
        text={t("pricingScreenHeroText")}
      />
      <div className="spacer-h-12" />

      <div className={styles.plansContainer}>

        <JinearFreePlan />

        <JinearProPlan
          actionButton={
            <>
              <div className={"spacer-h-4"} />
              <div className={"flex-1"} />
              <Button
                href={authState == "NOT_LOGGED_IN" ? "/login" : preferredRedirectRoute}
                variant={ButtonVariants.filled}
                onClick={onProPlanCtaClick}
              >
                <span>{authState == "NOT_LOGGED_IN" ? t("loginButtonLabel") : t("workspaceTierUpgradeButton")}</span>
              </Button>
              <div className={"spacer-h-2"} />
            </>
          }
        />

        <JinearPricingPlan
          planName={"Self-Hosted"}
          price={t("pricesPageBasicPrice")}
          description={t("pricesPageSelfHostDescription")}
          featuresTitle={t("pricesPageSelfHostFeature_additionalToProPlanText")}
        >
          <ul>
            <li>{t("pricesPageSelfHostFeature_Own")}</li>
            <li>{t("pricesPageSelfHostFeature_Privacy")}</li>
            <li>{t("pricesPageSelfHostFeature_EasyInstall")}</li>
          </ul>
          <div className={"spacer-h-4"} />
          <div className={"flex-1"} />
          <div className={styles.selfHostButtonsContainer}>
            <Button
              variant={ButtonVariants.contrast}
              className={"flex-1"}
              href={"https://github.com/140crafts/use-jinear"}
              target={"_blank"}
            >
              <FaGithub />&nbsp;<span>GitHub</span>
            </Button>
            <Button
              variant={ButtonVariants.contrast}
              className={"flex-1"}
              href={"https://gitlab.com/140crafts/use-jinear"}
              target={"_blank"}
            >
              <FaGitlab />&nbsp;<span>GitLab</span>
            </Button>
          </div>
          <div className={"spacer-h-2"} />
        </JinearPricingPlan>
      </div>

      <div className={"spacer-h-12"} />

      <div className={styles.comparePricingContainer}>
        <div className={styles.jinearCompareBox}>
          <div><FormLogo /></div>
          <h1 className={styles.jinearCompareBoxTitle}
              dangerouslySetInnerHTML={{ __html: t("pricingPageCompareJinearText") }}></h1>
        </div>

        <div className={styles.compareBox}>
          <div className={styles.compareBoxOtherProduct}>
            <SiSlack size={26} />
            <div className={styles.compareBoxOtherProductName}>
              <h2>Slack</h2>
              <span>{t("pricingPageComparePerMonthPerUserText").replace("{price}", "8")}</span>
            </div>
          </div>

          <div className={styles.compareBoxOtherProduct}>
            <SiDropbox size={26} />
            <div className={styles.compareBoxOtherProductName}>
              <h2>Dropbox</h2>
              <span>{t("pricingPageComparePerMonthPerUserText").replace("{price}", "15")}</span>
            </div>
          </div>

          <div className={styles.compareBoxOtherProduct}>
            <SiAsana size={26} />
            <div className={styles.compareBoxOtherProductName}>
              <h2>Asana</h2>
              <span>{t("pricingPageComparePerMonthPerUserText").replace("{price}", "10")}</span>
            </div>
          </div>

          <div className={styles.compareBoxOtherProduct} style={{ width: "100%" }}>
            <LuEqual size={26} />
            <div className={styles.compareBoxOtherProductName}>
              <h2>{t("pricingPageCompareTotalText")}</h2>
              <span>{t("pricingPageComparePerMonthPerUserText").replace("{price}", "33")}</span>
            </div>
          </div>

          <h1 className={styles.othersForTenUsers}>{t("pricingPageCompareTenUsersText")}</h1>
        </div>

      </div>

      <div className="spacer-h-12" />
      <Footer />
    </div>
  );
};

export default PricingPage;
