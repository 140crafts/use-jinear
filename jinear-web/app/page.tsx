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
import useTranslation from "locales/useTranslation";
import Image from "next/image";
import { useRouter } from "next/navigation";
import { useEffect } from "react";
import styles from "./index.module.scss";

export default function Home() {
  const pwa = isPwa();
  const router = useRouter();
  const authState = useTypedSelector(selectAuthState);
  const theme = useTheme();
  const { t } = useTranslation();

  useEffect(() => {
    if (pwa) {
      if (authState == "NOT_LOGGED_IN") {
        router.replace("/login");
      } else if (authState == "LOGGED_IN") {
        router.replace(ROUTE_IF_LOGGED_IN);
      }
    }
  }, [pwa, authState]);

  return pwa ? (
    <CircularLoading />
  ) : (
    <ClientOnly className={styles.container}>
      <HomePageNavbar />
      <Hero />
      <CallToActionBar authState={authState} />
      <div className="spacer-h-4" />

      <div className={styles.heroImageContainer}>
        <Image
          alt="Jinear app image"
          src={`https://storage.googleapis.com/jinear-b0/web-assets/jinear-homescreen-images/home-${theme}.png`}
          fill={true}
          objectFit="contain"
        />
      </div>

      <div className="spacer-h-4" />
      <Line />
      <div className="spacer-h-2" />

      <FeatureCard
        title={t("homePageFeatureTitle_Calendar")}
        text={t("homePageFeatureText_Calendar")}
        imageUrl={`https://storage.googleapis.com/jinear-b0/web-assets/jinear-homescreen-images/home-week-${theme}.png`}
      />

      <div className="spacer-h-4" />
      <Line />
      <div className="spacer-h-2" />

      <FeatureCard
        title={t("homePageFeatureTitle_Task")}
        text={t("homePageFeatureText_Task")}
        imageUrl={`https://storage.googleapis.com/jinear-b0/web-assets/jinear-homescreen-images/tasks-${theme}.png`}
      />

      <div className="spacer-h-4" />
      <Line />
      <div className="spacer-h-2" />

      <FeatureCard
        title={t("homePageFeatureTitle_Feed_GMail")}
        text={t("homePageFeatureText_Feed_Mail")}
        imageUrl={`https://storage.googleapis.com/jinear-b0/web-assets/jinear-homescreen-images/feed2-${theme}.png`}
      />
    </ClientOnly>
  );
}
