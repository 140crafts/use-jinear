"use client";
import CircularLoading from "@/components/circularLoading/CircularLoading";
import ClientOnly from "@/components/clientOnly/ClientOnly";
import CallToActionBar from "@/components/homepage/callToActionBar/CallToActionBar";
import FeatureCard from "@/components/homepage/featureCard/FeatureCard";
import Hero from "@/components/homepage/hero/Hero";
import HomePageNavbar from "@/components/homepage/navbar/HomePageNavbar";
import Line from "@/components/line/Line";
import { useTheme } from "@/components/themeProvider/ThemeProvider";
import { selectAuthState } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { ROUTE_IF_LOGGED_IN } from "@/utils/constants";
import isPwa from "@/utils/pwaHelper";
import { isWebView } from "@/utils/webviewUtils";
import useTranslation from "locales/useTranslation";
import Image from "next/image";
import { useRouter } from "next/navigation";
import { useEffect } from "react";
import styles from "./index.module.scss";
import FeatureVideoCard from "@/components/homepage/featureVideoCard/FeatureVideoCard";

export default function Home() {
  const pwa = isPwa();
  const isWebApp = isWebView();
  const isMobileApp = pwa || isWebApp;

  const router = useRouter();
  const authState = useTypedSelector(selectAuthState);
  const theme = useTheme();
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
      <Hero />
      <CallToActionBar authState={authState} />
      <div className="spacer-h-4" />

      <div className={styles.heroVideoContainer}>
        <iframe
          src={`https://player.vimeo.com/video/${theme == "light" ? "988098899" : "988098825"}?badge=0&autoplay=1&loop=1&background=1&amp;autopause=0&muted=1&amp;player_id=0&amp;app_id=58479`}
          frameBorder="0" allow="autoplay; fullscreen; picture-in-picture; clipboard-write"
          className={styles.vimeoIframe} title="task-create-dark"></iframe>
      </div>

      <div className="spacer-h-4" />
      <Line />
      <div className="spacer-h-4" />

      <FeatureVideoCard
        title={t("homePageFeatureTitle_NewTeam")}
        text={t("homePageFeatureText_NewTeam")}
        vimeoIdLight={"988098284"}
        vimeoIdDark={"988098247"}
      />

      <div className="spacer-h-4" />
      <Line />
      <div className="spacer-h-4" />

      <FeatureVideoCard
        title={t("homePageFeatureTitle_ListAndFilterYourTasks")}
        text={t("homePageFeatureText_ListAndFilterYourTasks")}
        vimeoIdLight={"988098780"}
        vimeoIdDark={"988098743"}
      />

      <div className="spacer-h-4" />
      <Line />
      <div className="spacer-h-4" />

      <FeatureVideoCard
        title={t("homePageFeatureTitle_Reminder")}
        text={t("homePageFeatureText_Reminder")}
        vimeoIdLight={"988098452"}
        vimeoIdDark={"988098405"}
      />

      <div className="spacer-h-4" />
      <Line />
      <div className="spacer-h-4" />

      <FeatureVideoCard
        title={t("homePageFeatureTitle_FilesAndComments")}
        text={t("homePageFeatureText_FilesAndComments")}
        vimeoIdLight={"988098180"}
        vimeoIdDark={"988098131"}
      />

      <div className="spacer-h-4" />
      <Line />
      <div className="spacer-h-4" />

      <FeatureVideoCard
        title={t("homePageFeatureTitle_Share")}
        text={t("homePageFeatureText_Share")}
        vimeoIdLight={"988098610"}
        vimeoIdDark={"988098554"}
      />
      <div className="spacer-h-4" />
      <Line />
      <div className="spacer-h-4" />

      <FeatureVideoCard
        title={t("homePageFeatureTitle_GoogleCalendar")}
        text={t("homePageFeatureText_GoogleCalendar")}
        vimeoIdLight={"988098695"}
        vimeoIdDark={"988098661"}
      />
      <div className="spacer-h-4" />
    </ClientOnly>
  );
}
