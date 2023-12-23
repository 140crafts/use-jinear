"use client";
import CircularLoading from "@/components/circularLoading/CircularLoading";
import CallToActionBar from "@/components/homepage/callToActionBar/CallToActionBar";
import FeatureIcons from "@/components/homepage/featureIcons/FeatureIcons";
import Hero from "@/components/homepage/hero/Hero";
import HomePageNavbar from "@/components/homepage/navbar/HomePageNavbar";
import { selectAuthState } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { ROUTE_IF_LOGGED_IN } from "@/utils/constants";
import isPwa from "@/utils/pwaHelper";
import { useRouter } from "next/navigation";
import { useEffect } from "react";
import styles from "./index.module.scss";

const ICON_SIZE = 42;

export default function Home() {
  const pwa = isPwa();
  const router = useRouter();
  const authState = useTypedSelector(selectAuthState);

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
    <div className={styles.container}>
      <HomePageNavbar />
      <Hero />
      <CallToActionBar authState={authState} />
      <div className="spacer-h-4" />
      <FeatureIcons />
    </div>
  );
}
