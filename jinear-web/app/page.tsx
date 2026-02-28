"use client";
import CircularLoading from "@/components/circularLoading/CircularLoading";
import ClientOnly from "@/components/clientOnly/ClientOnly";
import CallToActionBar from "@/components/homepage/callToActionBar/CallToActionBar";
import Hero from "@/components/homepage/hero/Hero";
import HomePageNavbar from "@/components/homepage/navbar/HomePageNavbar";
import { useTheme } from "@/components/themeProvider/ThemeProvider";
import { selectAuthState } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import isPwa from "@/utils/pwaHelper";
import { isWebView } from "@/utils/webviewUtils";
import useTranslation from "locales/useTranslation";
import { useRouter } from "next/navigation";
import React, { useEffect } from "react";
import styles from "./index.module.scss";
import HeroFeatures from "@/components/homepage/heroFeatures/HeroFeatures";
import FeatureCard from "@/components/homepage/featureCard/FeatureCard";
import SocialProofBar from "@/components/homepage/socialProofBar/SocialProofBar";
import ProblemBar from "@/components/homepage/problemBar/ProblemBar";
import SelfHostHero from "@/components/homepage/selfHostHero/SelfHostHero";
import ComplianceSection from "@/components/homepage/complianceSection/ComplianceSection";
import ComparisonSection from "@/components/homepage/comparisonSection/ComparisonSection";
import OpenSourceSection from "@/components/homepage/openSourceSection/OpenSourceSection";
import BottomCta from "@/components/homepage/bottomCta/BottomCta";
import Footer from "@/components/homepage/footer/Footer";
import {
  LuCalendarCheck2,
  LuCheckCircle as LuCircleCheck,
  LuClipboardList, LuDatabase,
  LuFile, LuHash,
  LuLayoutDashboard,
  LuMessageSquare, LuPin, LuSend, LuShieldCheck, LuUsers
} from "react-icons/lu";
import {
  SiApple,
  SiAsana,
  SiBasecamp, SiBox,
  SiDiscord, SiDropbox,
  SiGooglecalendar, SiGoogledrive,
  SiJira,
  SiSlack,
  SiWhatsapp
} from "react-icons/si";
import { useRouteIfLoggedIn } from "@/hooks/useRouteIfLoggedIn";

export default function Home() {
  const pwa = isPwa();
  const isWebApp = isWebView();
  const isMobileApp = pwa || isWebApp;

  const router = useRouter();
  const authState = useTypedSelector(selectAuthState);
  const theme = useTheme();
  const heroImageSrcType = theme == "light" ? "light" : "dark";
  const { t } = useTranslation();

  const routeIfLoggedIn = useRouteIfLoggedIn();

  useEffect(() => {
    if (isMobileApp) {
      if (authState == "NOT_LOGGED_IN") {
        router.replace("/login");
      } else if (authState == "LOGGED_IN") {
        router.replace(routeIfLoggedIn);
      }
    }
  }, [isMobileApp, authState, routeIfLoggedIn]);

  return isMobileApp ? (
    <CircularLoading />
  ) : (
    <ClientOnly className={styles.container}>
      <HomePageNavbar />

      <div className="spacer-h-12" />
      <div className="spacer-h-6" />

      <Hero
        title1={t("homescreenHeroTitleLine1")}
        text={t("homescreenHeroText")}
      />

      <div className="spacer-h-4" />
      <CallToActionBar authState={authState} />
      <div className="spacer-h-6" />

      <SocialProofBar />

      <div className="spacer-h-12" />
      <hr className={styles.divider} />
      <div className="spacer-h-12" />

      {/*<ProblemBar />*/}

      {/*<div className="spacer-h-12" />*/}
      {/*<hr className={styles.divider} />*/}
      {/*<div className="spacer-h-12" />*/}

      <SelfHostHero />

      <div className="spacer-h-12" />
      <hr className={styles.divider} />
      <div className="spacer-h-12" />

      <p className={styles.eyebrow}>{t("landingPageHeroFeaturesEyebrow")}</p>
      <h2 className={styles.sectionTitle}>{t("landingPageHeroFeaturesTitle")}</h2>

      <div className="spacer-h-6" />
      <HeroFeatures />

      <div className="spacer-h-12" />
      <hr className={styles.divider} />
      <div className="spacer-h-12" />

      <FeatureCard
        title1={t("landingPageFeature1")}
        text={t("landingPageFeature1Text")}
        imageUrl={`https://storage.googleapis.com/jinear-b0/web-assets/jinear-homescreen-images/v2/projects-${heroImageSrcType}.png`}
        featureCardIconInfoList={[
          { id: "task-todos", Icon: LuCircleCheck, title: t("landingPageFeature1Sub1Title"), text: t("landingPageFeature1Sub1Text") },
          { id: "task-todos2", Icon: LuFile, title: t("landingPageFeature1Sub2Title"), text: t("landingPageFeature1Sub2Text") },
          { id: "task-todos3", Icon: LuMessageSquare, title: t("landingPageFeature1Sub3Title"), text: t("landingPageFeature1Sub3Text") }
        ]}
        alternativeToLabel={t("landingPageFeature1AlternativeTo")}
        alternativeToInfoList={[
          { id: "alternative-to-3", Icon: SiBasecamp, name: "Basecamp" },
          { id: "alternative-to-1", Icon: SiAsana, name: "Asana" },
          { id: "alternative-to-2", Icon: SiJira, name: "Jira" }
        ]}
      />

      <div className="spacer-h-12" />
      <hr className={styles.divider} />
      <div className="spacer-h-12" />

      <FeatureCard
        title1={t("landingPageFeatureChat")}
        text={t("landingPageFeatureChatText")}
        imageUrl={`https://storage.googleapis.com/jinear-b0/web-assets/jinear-homescreen-images/v2/chat-${heroImageSrcType}.png`}
        featureCardIconInfoList={[
          { id: "feature-chat", Icon: LuHash, title: t("landingPageFeatureChatSub1Title"), text: t("landingPageFeatureChatSub1Text") },
          { id: "feature-chat-2", Icon: LuSend, title: t("landingPageFeatureChatSub2Title"), text: t("landingPageFeatureChatSub2Text") },
          { id: "feature-chat-3", Icon: LuPin, title: t("landingPageFeatureChatSub3Title"), text: t("landingPageFeatureChatSub3Text") }
        ]}
        alternativeToLabel={t("landingPageFeatureChatAlternativeTo")}
        alternativeToInfoList={[
          { id: "chat-alternative-to-1", Icon: SiSlack, name: "Slack" },
          { id: "chat-alternative-to-2", Icon: SiDiscord, name: "Discord" },
          { id: "chat-alternative-to-3", Icon: SiWhatsapp, name: "Whatsapp" }
        ]}
      />

      <div className="spacer-h-12" />
      <hr className={styles.divider} />
      <div className="spacer-h-12" />

      <FeatureCard
        title1={t("landingPageFeatureStorage")}
        text={t("landingPageFeatureStorageText")}
        imageUrl={`https://storage.googleapis.com/jinear-b0/web-assets/jinear-homescreen-images/v2/files-2-${heroImageSrcType}.png`}
        featureCardIconInfoList={[
          { id: "feature-storage", Icon: LuUsers, title: t("landingPageFeatureStorageSub1Title"), text: t("landingPageFeatureStorageSub1Text") },
          { id: "feature-storage-2", Icon: LuDatabase, title: t("landingPageFeatureStorageSub2Title"), text: t("landingPageFeatureStorageSub2Text") },
          { id: "feature-storage-3", Icon: LuShieldCheck, title: t("landingPageFeatureStorageSub3Title"), text: t("landingPageFeatureStorageSub3Text") }
        ]}
        alternativeToLabel={t("landingPageFeatureStorageAlternativeTo")}
        alternativeToInfoList={[
          { id: "storage-alternative-to-1", Icon: SiDropbox, name: "Dropbox" },
          { id: "storage-alternative-to-2", Icon: SiGoogledrive, name: "Google Drive" },
          { id: "storage-alternative-to-3", Icon: SiBox, name: "Box" }
        ]}
      />

      <div className="spacer-h-12" />
      <hr className={styles.divider} />
      <div className="spacer-h-12" />

      <FeatureCard
        title1={t("landingPageFeature2")}
        text={t("landingPageFeature2Text")}
        imageUrl={`https://storage.googleapis.com/jinear-b0/web-assets/jinear-homescreen-images/v2/calendar-${heroImageSrcType}.png`}
        featureCardIconInfoList={[
          { id: "feature-calendar", Icon: LuCalendarCheck2, title: t("landingPageFeature2Sub1Title"), text: t("landingPageFeature2Sub1Text") },
          { id: "feature-calendar-2", Icon: LuClipboardList, title: t("landingPageFeature2Sub2Title"), text: t("landingPageFeature2Sub2Text") },
          { id: "feature-calendar-3", Icon: LuLayoutDashboard, title: t("landingPageFeature2Sub3Title"), text: t("landingPageFeature2Sub3Text") }
        ]}
        alternativeToLabel={t("landingPageFeature2AlternativeTo")}
        alternativeToInfoList={[
          { id: "calendar-alternative-to-1", Icon: SiGooglecalendar, name: "Google Calendar" },
          { id: "calendar-alternative-to-2", Icon: SiApple, name: "Apple Calendar" }
        ]}
      />

      <div className="spacer-h-12" />
      <hr className={styles.divider} />
      <div className="spacer-h-12" />

      <ComplianceSection />

      <div className="spacer-h-12" />
      <hr className={styles.divider} />
      <div className="spacer-h-12" />

      <ComparisonSection />

      <div className="spacer-h-12" />
      <hr className={styles.divider} />
      <div className="spacer-h-12" />

      <OpenSourceSection />

      <div className="spacer-h-12" />
      <hr className={styles.divider} />
      <div className="spacer-h-12" />

      <BottomCta />

      <div className="spacer-h-12" />

      <Footer />
    </ClientOnly>
  );
}
