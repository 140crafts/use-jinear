"use client";
import CircularLoading from "@/components/circularLoading/CircularLoading";
import ClientOnly from "@/components/clientOnly/ClientOnly";
import CallToActionBar from "@/components/homepage/callToActionBar/CallToActionBar";
import Hero from "@/components/homepage/hero/Hero";
import HomePageNavbar from "@/components/homepage/navbar/HomePageNavbar";
import { useTheme } from "@/components/themeProvider/ThemeProvider";
import { selectAuthState } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { ROUTE_IF_LOGGED_IN } from "@/utils/constants";
import isPwa from "@/utils/pwaHelper";
import { isWebView } from "@/utils/webviewUtils";
import useTranslation from "locales/useTranslation";
import { useRouter } from "next/navigation";
import React, { useEffect } from "react";
import styles from "./index.module.scss";
import Button, { ButtonVariants } from "@/components/button";
import HeroFeatures from "@/components/homepage/heroFeatures/HeroFeatures";
import SubHero from "@/components/homepage/subHero/SubHero";
import FeatureCard from "@/components/homepage/featureCard/FeatureCard";
import {
  LuCalendarCheck2,
  LuCheckCircle as LuCircleCheck,
  LuClipboardList,
  LuFile,
  LuLayoutDashboard,
  LuMessageSquare
} from "react-icons/lu";
import { SiApple, SiAsana, SiBasecamp, SiGooglecalendar, SiJira } from "react-icons/si";
import Footer from "@/components/homepage/footer/Footer";
import OrLine from "@/components/orLine/OrLine";
import { FaGithub, FaGitlab } from "react-icons/fa6";
import { IoBalloon } from "react-icons/io5";

export default function Home() {
  const pwa = isPwa();
  const isWebApp = isWebView();
  const isMobileApp = pwa || isWebApp;

  const router = useRouter();
  const authState = useTypedSelector(selectAuthState);
  const theme = useTheme();
  const heroImageSrcType = theme == "light" ? "light" : "dark";
  const { t } = useTranslation();

  useEffect(() => {
    if (isMobileApp) {
      if (authState == "NOT_LOGGED_IN") {
        router.replace("/login");
      } else if (authState == "LOGGED_IN") {
        router.replace(ROUTE_IF_LOGGED_IN);
      }
    }
  }, [isMobileApp, authState]);

  return isMobileApp ? (
    <CircularLoading />
  ) : (
    <ClientOnly className={styles.container}>
      <HomePageNavbar />

      <div className="spacer-h-12" />
      <Hero title1={t("homescreenHeroTitleLine1")} title2={t("homescreenHeroTitleLine2")}
            text={t("homescreenHeroText")} />
      <div className="spacer-h-2" />
      <CallToActionBar authState={authState} />
      <div className="spacer-h-2" />
      <OrLine />
      <div className="spacer-h-2" />
      <div className={styles.selfHostContainer}>
        <span>
          {t("homescreenSelfHostText")}
          <IoBalloon
            className={"icon"}
            style={{ display: "inline-block", marginInlineStart: ".25rem" }}
          />
        </span>
        <div className={styles.selfHostButtonsContainer}>
          <Button variant={ButtonVariants.outline} href={"https://github.com/140crafts/use-jinear"} target={"_blank"}>
            <FaGithub />&nbsp;<span>GitHub</span>
          </Button>
          <Button variant={ButtonVariants.outline} href={"https://gitlab.com/140crafts/use-jinear"} target={"_blank"}>
            <FaGitlab />&nbsp;<span>GitLab</span>
          </Button>
        </div>
      </div>
      <div className="spacer-h-12" />

      <HeroFeatures />

      <div className="spacer-h-12" />
      <div className="spacer-h-12" />

      <FeatureCard
        title1={t("landingPageFeature1")}
        text={t("landingPageFeature1Text")}
        imageUrl={`https://storage.googleapis.com/jinear-b0/web-assets/jinear-homescreen-images/v2/projects-${heroImageSrcType}.png`}
        featureCardIconInfoList={[
          {
            id: "task-todos",
            Icon: LuCircleCheck,
            title: t("landingPageFeature1Sub1Title"),
            text: t("landingPageFeature1Sub1Text")
          },
          {
            id: "task-todos2",
            Icon: LuFile,
            title: t("landingPageFeature1Sub2Title"),
            text: t("landingPageFeature1Sub2Text")
          },
          {
            id: "task-todos3",
            Icon: LuMessageSquare,
            title: t("landingPageFeature1Sub3Title"),
            text: t("landingPageFeature1Sub3Text")
          }
        ]}
        alternativeToLabel={t("landingPageFeature1AlternativeTo")}
        alternativeToInfoList={[
          { id: "alternative-to-3", Icon: SiBasecamp, name: "Basecamp" },
          { id: "alternative-to-1", Icon: SiAsana, name: "Asana" },
          { id: "alternative-to-2", Icon: SiJira, name: "Jira" }
        ]}
      />

      <div className="spacer-h-12" />
      <div className="spacer-h-12" />

      <FeatureCard
        title1={t("landingPageFeature2")}
        text={t("landingPageFeature2Text")}
        imageUrl={`https://storage.googleapis.com/jinear-b0/web-assets/jinear-homescreen-images/v2/calendar-${heroImageSrcType}.png`}
        featureCardIconInfoList={[
          {
            id: "feature-calendar",
            Icon: LuCalendarCheck2,
            title: t("landingPageFeature2Sub1Title"),
            text: t("landingPageFeature2Sub1Text")
          },
          {
            id: "feature-calendar-2",
            Icon: LuClipboardList,
            title: t("landingPageFeature2Sub2Title"),
            text: t("landingPageFeature2Sub2Text")
          },
          {
            id: "feature-calendar-3",
            Icon: LuLayoutDashboard,
            title: t("landingPageFeature2Sub3Title"),
            text: t("landingPageFeature2Sub3Text")
          }
        ]}
        alternativeToLabel={t("landingPageFeature2AlternativeTo")}
        alternativeToInfoList={[
          { id: "calendar-alternative-to-1", Icon: SiGooglecalendar, name: "Google Calendar" },
          { id: "calendar-alternative-to-2", Icon: SiApple, name: "Apple Calendar" }
        ]}
      />

      <div className="spacer-h-12" />
      <div className="spacer-h-12" />

      <SubHero
        className={styles.startFree}
        title1={t("landingPageStartFreeTitle")}
        title2={t("landingPageStartFreeTitle2")}
        text={t("landingPageStartFreeText")}
      />


      <div className="spacer-h-12" />

      {/*<VideoHeroSection title1={"title1"} text={"text"}/>*/}
      {/*<MobileAppSection />*/}

      <div className="spacer-h-12" />

      <Footer />
    </ClientOnly>
  );
}
