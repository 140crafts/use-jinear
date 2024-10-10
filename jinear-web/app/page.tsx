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
import Button, { ButtonHeight } from "@/components/button";
import {
  LuAlarmClock,
  LuBox,
  LuCalendarSearch,
  LuCheckSquare,
  LuFile,
  LuList,
  LuMessageSquare,
  LuRss,
  LuUsers
} from "react-icons/lu";
import { SiGooglecalendar } from "react-icons/si";

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

      {/*@formatter:off*/}
      <div className={styles.featuresNavbar}>
        <Button className={styles.navButton} heightVariant={ButtonHeight.short} href={"#feature-projects"}>
          <LuBox className={'icon'}/>
          <span>{t("homePageFeatureTitle_NewProject")}</span>
        </Button>
        <Button className={styles.navButton} heightVariant={ButtonHeight.short} href={"#feature-project-feed-post"}>
          <LuRss className={'icon'} />
          <span>{t('homePageFeatureTitle_NewProjectFeedPost')}</span>
        </Button>
        <Button className={styles.navButton} heightVariant={ButtonHeight.short} href={"#feature-new-team"}>
          <LuUsers className={'icon'} />
          <span>{t('homePageFeatureTitle_NewTeam')}</span>
        </Button>
        <Button className={styles.navButton} heightVariant={ButtonHeight.short} href={"#feature-tasks"}>
          <LuCheckSquare className={'icon'} />
          <span>{t('homePageFeatureTitle_Task')}</span>
        </Button>
        <Button className={styles.navButton} heightVariant={ButtonHeight.short} href={"#feature-task-filter"}>
          <LuList className={'icon'} />
          <span>{t('homePageFeatureTitle_ListAndFilterYourTasks')}</span>
        </Button>
        <Button className={styles.navButton} heightVariant={ButtonHeight.short} href={"#feature-reminders"}>
          <LuAlarmClock className={'icon'} />
          <span>{t('homePageFeatureTitle_Reminder')}</span>
        </Button>
        <Button className={styles.navButton} heightVariant={ButtonHeight.short} href={"#feature-files-and-comments"}>
          <LuFile className={'icon'} />
          <LuMessageSquare className={'icon'} />
          <span>{t('homePageFeatureTitle_FilesAndComments')}</span>
        </Button>
        <Button className={styles.navButton} heightVariant={ButtonHeight.short} href={"#feature-calendar-share"}>
          <LuCalendarSearch className={'icon'} />
          <span>{t('homePageFeatureTitle_Share')}</span>
        </Button>
        <Button className={styles.navButton} heightVariant={ButtonHeight.short} href={"#feature-google-calendar-support"}>
          <SiGooglecalendar className={'icon'} />
          <span>{t("homePageFeatureTitle_GoogleCalendar")}</span>
        </Button>
      </div>
      {/*@formatter:on*/}

      <div className="spacer-h-4" />

      <div id={"feature-projects"} className={styles.heroVideoContainer}>
        <iframe
          src={`https://player.vimeo.com/video/${theme == "light" ? "1018031703" : "1018031575"}?badge=0&autoplay=1&loop=1&background=1&amp;autopause=0&muted=1&amp;player_id=0&amp;app_id=58479`}
          frameBorder="0" allow="autoplay; fullscreen; picture-in-picture; clipboard-write"
          className={styles.vimeoIframe} title="task-create-dark"></iframe>
      </div>

      {/*<FeatureVideoCard*/}
      {/*  title={t("homePageFeatureTitle_NewProject")}*/}
      {/*  text={t("homePageFeatureText_NewProject")}*/}
      {/*  vimeoIdLight={"1018031703"}*/}
      {/*  vimeoIdDark={"1018031575"}*/}
      {/*/>*/}

      <div id={"feature-project-feed-post"} />
      <div className="spacer-h-4" />
      <Line />
      <div className="spacer-h-4" />

      <FeatureVideoCard
        title={t("homePageFeatureTitle_NewProjectFeedPost")}
        text={t("homePageFeatureText_NewProjectFeedPost")}
        vimeoIdLight={"1018032005"}
        vimeoIdDark={"1018031810"}
      />

      <div id={"feature-new-team"} />
      <div className="spacer-h-4" />
      <Line />
      <div className="spacer-h-4" />

      <FeatureVideoCard
        id={"feature-new-team"}
        title={t("homePageFeatureTitle_NewTeam")}
        text={t("homePageFeatureText_NewTeam")}
        vimeoIdLight={"988098284"}
        vimeoIdDark={"988098247"}
      />

      <div id={"feature-tasks"} />
      <div className="spacer-h-4" />
      <Line />
      <div className="spacer-h-4" />

      <FeatureVideoCard
        title={t("homePageFeatureTitle_Task")}
        text={t("homePageFeatureText_Task")}
        vimeoIdLight={"988098899"}
        vimeoIdDark={"988098825"}
      />

      <div id={"feature-task-filter"} />
      <div className="spacer-h-4" />
      <Line />
      <div className="spacer-h-4" />

      <FeatureVideoCard
        title={t("homePageFeatureTitle_ListAndFilterYourTasks")}
        text={t("homePageFeatureText_ListAndFilterYourTasks")}
        vimeoIdLight={"988098780"}
        vimeoIdDark={"988098743"}
      />

      <div id={"feature-reminders"} />
      <div className="spacer-h-4" />
      <Line />
      <div className="spacer-h-4" />

      <FeatureVideoCard
        title={t("homePageFeatureTitle_Reminder")}
        text={t("homePageFeatureText_Reminder")}
        vimeoIdLight={"988098452"}
        vimeoIdDark={"988098405"}
      />

      <div id={"feature-files-and-comments"} />
      <div className="spacer-h-4" />
      <Line />
      <div className="spacer-h-4" />

      <FeatureVideoCard
        title={t("homePageFeatureTitle_FilesAndComments")}
        text={t("homePageFeatureText_FilesAndComments")}
        vimeoIdLight={"988098180"}
        vimeoIdDark={"988098131"}
      />

      <div id={"feature-calendar-share"} />
      <div className="spacer-h-4" />
      <Line />
      <div className="spacer-h-4" />

      <FeatureVideoCard
        title={t("homePageFeatureTitle_Share")}
        text={t("homePageFeatureText_Share")}
        vimeoIdLight={"988098610"}
        vimeoIdDark={"988098554"}
      />

      <div id={"feature-google-calendar-support"} />
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
