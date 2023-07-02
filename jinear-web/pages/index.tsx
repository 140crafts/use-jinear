import Button, { ButtonVariants } from "@/components/button";
import CircularLoading from "@/components/circularLoading/CircularLoading";
import FormLogo from "@/components/formLogo/FormLogo";
import ThemeToggle from "@/components/themeToggle/ThemeToggle";
import { selectAuthState } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { ROUTE_IF_LOGGED_IN } from "@/utils/constants";
import isPwa from "@/utils/pwaHelper";
import useTranslation from "locales/useTranslation";
import { useRouter } from "next/router";
import { useEffect } from "react";
import { useTheme } from "./_app";
import styles from "./index.module.scss";

export default function Home() {
  const theme = useTheme();
  const pwa = isPwa();
  const router = useRouter();
  const { t } = useTranslation();
  const authState = useTypedSelector(selectAuthState);

  const homeScreen = `/_next/image?url=%2Fimages%2Fhomescreen%2Fhome-${theme}.png&w=640&q=75 640w, /_next/image?url=%2Fimages%2Fhomescreen%2Fhome-${theme}.png&w=750&q=75 750w, /_next/image?url=%2Fimages%2Fhomescreen%2Fhome-${theme}.png&w=828&q=75 828w, /_next/image?url=%2Fimages%2Fhomescreen%2Fhome-${theme}.png&w=1080&q=75 1080w, /_next/image?url=%2Fimages%2Fhomescreen%2Fhome-${theme}.png&w=1200&q=75 1200w, /_next/image?url=%2Fimages%2Fhomescreen%2Fhome-${theme}.png&w=1920&q=75 1920w, /_next/image?url=%2Fimages%2Fhomescreen%2Fhome-${theme}.png&w=2048&q=75 2048w, /_next/image?url=%2Fimages%2Fhomescreen%2Fhome-${theme}.png&w=3840&q=75 3840w`;

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
      <div className={styles.header}>
        <FormLogo />
        <div className={styles.headerActionBar}>
          <Button href="/pricing">
            <b>{t("homescreenActionBarPricing")}</b>
          </Button>
          <ThemeToggle />
        </div>
      </div>

      <div className={styles.heroContainer}>
        <span className={styles.heroTitle}>
          {t("homescreenHeroTitleLine1")} <br />
          {t("homescreenHeroTitleLine2")}
        </span>

        <span className={styles.heroText}>{t("homescreenHeroText")}</span>
      </div>

      <div className={styles.actionBar}>
        {authState == "LOGGED_IN" && (
          <Button variant={ButtonVariants.contrast} href={ROUTE_IF_LOGGED_IN}>
            {t("homescreenGoToApp")}
          </Button>
        )}
        {authState == "NOT_LOGGED_IN" && (
          <>
            <Button variant={ButtonVariants.contrast} href={"/login"}>
              {t("homescreenLogin")}
            </Button>
            <Button variant={ButtonVariants.filled} href={"/register"}>
              {t("homescreenRegister")}
            </Button>
          </>
        )}
        {authState == "NOT_DECIDED" && <CircularLoading />}
      </div>

      <div className="spacer-h-6" />

      <div className={styles.section}>
        <img className={styles.image} srcSet={homeScreen} />
      </div>
    </div>
  );
}
