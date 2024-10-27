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
import { useEffect } from "react";
import styles from "./index.module.scss";
import VideoHeroSection from "@/components/homepage/videoHeroSection/VideoHeroSection";

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
      <VideoHeroSection />
      <div className="spacer-h-4" />
      <div className="spacer-h-4" />
    </ClientOnly>
  );
}
