"use client";
import Button, { ButtonVariants } from "@/components/button";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./index.module.scss";
import JinearPricingPlan from "@/components/jinearPricingPlan/JinearPricingPlan";
import JinearFreePlan from "@/components/jinearPricingPlan/jinearFreePlan/JinearFreePlan";
import JinearProPlan from "@/components/jinearPricingPlan/jinearProPlan/JinearProPlan";
import { FaGithub, FaGitlab } from "react-icons/fa6";
import HomePageNavbar from "@/components/homepage/navbar/HomePageNavbar";
import Hero from "@/components/homepage/hero/Hero";
import Footer from "@/components/homepage/footer/Footer";
import FormLogo from "@/components/formLogo/FormLogo";
import { SiAsana, SiDropbox, SiSlack } from "react-icons/si";
import { LuEqual } from "react-icons/lu";
import { APP_URL, GITHUB_URL, GITLAB_URL } from "@/utils/constants";

const PricingClient: React.FC = () => {
  const { t } = useTranslation();

  return (
    <div className={styles.container}>
      <HomePageNavbar />

      <div className="spacer-h-12" />
      <Hero
        title1={`${t("pricingScreenHeroTitleLine1")} ${t("pricingScreenHeroTitleLine2")}`}
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
              <Button href={`${APP_URL}/register`} target={"_blank"} variant={ButtonVariants.filled}>
                <span>{t("registerWithMailFormTitle")}</span>
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
            <Button variant={ButtonVariants.contrast} className={"flex-1"} href={GITHUB_URL} target={"_blank"}>
              <FaGithub />&nbsp;<span>GitHub</span>
            </Button>
            <Button variant={ButtonVariants.contrast} className={"flex-1"} href={GITLAB_URL} target={"_blank"}>
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
          <h1
            className={styles.jinearCompareBoxTitle}
            dangerouslySetInnerHTML={{ __html: t("pricingPageCompareJinearText") }}
          ></h1>
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

export default PricingClient;
