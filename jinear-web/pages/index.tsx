import Button, { ButtonVariants } from "@/components/button";
import CircularLoading from "@/components/circularLoading/CircularLoading";
import FormLogo from "@/components/formLogo/FormLogo";
import Line from "@/components/line/Line";
import ThemeToggle from "@/components/themeToggle/ThemeToggle";
import { selectAuthState } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { ROUTE_IF_LOGGED_IN } from "@/utils/constants";
import isPwa from "@/utils/pwaHelper";
import useTranslation from "locales/useTranslation";
import Image from "next/image";
import { useRouter } from "next/router";
import { useEffect } from "react";
import {
  IoAlarmOutline,
  IoArrowForward,
  IoCalendarNumberOutline,
  IoCheckmarkCircleOutline,
  IoPeopleOutline,
  IoReaderOutline,
} from "react-icons/io5";
import { useTheme } from "./_app";
import styles from "./index.module.scss";

const ICON_SIZE = 42;

export default function Home() {
  const theme = useTheme();
  const pwa = isPwa();
  const router = useRouter();
  const { t } = useTranslation();
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
          <Button variant={ButtonVariants.contrast} href={ROUTE_IF_LOGGED_IN} className={styles.goToAppButton}>
            {t("homescreenGoToApp")}
            <IoArrowForward />
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

      <div className={styles.sectionContainer}>
        <Line />
        <div className={styles.section}>
          <IoCalendarNumberOutline size={ICON_SIZE} />
          <h1>{t("homescreenCardTitle_Calendar")}</h1>
          <span>{t("homescreenCardText_Calendar")}</span>
          <div className={styles.imageContainer}>
            <Image
              alt={""}
              src={`/images/homescreen/home-${theme}.png`}
              width={550}
              height={550}
              // sizes="(min-width: 525px) 95vw, 70vw"
              className={styles.image}
            />
          </div>
        </div>

        <Line />

        <div className={styles.section}>
          <IoCheckmarkCircleOutline size={ICON_SIZE} />
          <h1>{t("homescreenCardTitle_NewTask")}</h1>
          <span>{t("homescreenCardText_NewTask")}</span>
          <div className={styles.imageContainer}>
            <Image
              alt={""}
              src={`/images/homescreen/new-task-${theme}.png`}
              width={550}
              height={550}
              sizes="(min-width: 525px) 95vw, 70vw"
              className={styles.image}
            />
          </div>
        </div>

        <Line />

        <div className={styles.section}>
          <IoAlarmOutline size={ICON_SIZE} />

          <h1>{t("homescreenCardTitle_Reminder")}</h1>
          <span>{t("homescreenCardText_Reminder")}</span>
          <div className={styles.imageContainer}>
            <Image
              alt={""}
              src={`/images/homescreen/reminders-${theme}.png`}
              width={550}
              height={550}
              sizes="(min-width: 525px) 95vw, 70vw"
              className={styles.image}
            />
          </div>
        </div>

        <Line />

        <div className={styles.section}>
          <IoPeopleOutline size={ICON_SIZE} />
          <h1>{t("homescreenCardTitle_CollabWorkspace")}</h1>
          <span>{t("homescreenCardText_CollabWorkspace")}</span>
          <div className={styles.imageContainer}>
            <Image
              alt={""}
              src={`/images/homescreen/invite-${theme}.png`}
              width={550}
              height={550}
              sizes="(min-width: 525px) 95vw, 70vw"
              className={styles.image}
            />
          </div>
        </div>

        <Line />

        <div className={styles.section}>
          <IoReaderOutline size={ICON_SIZE} />
          <h1>{t("homescreenCardTitle_Boards")}</h1>
          <span>{t("homescreenCardText_Boards")}</span>
          <div className={styles.imageContainer}>
            <Image
              alt={""}
              src={`/images/homescreen/board-${theme}.png`}
              width={550}
              height={550}
              sizes="(min-width: 525px) 95vw, 70vw"
              className={styles.image}
            />
          </div>
        </div>

        <Line />

        <div className={styles.section}>
          <IoCheckmarkCircleOutline size={ICON_SIZE} />
          <h1>{t("homescreenCardTitle_Task")}</h1>
          <span>{t("homescreenCardText_Task")}</span>
          <Image
            alt={""}
            src={`/images/homescreen/task-detail-${theme}.png`}
            width={550}
            height={550}
            sizes="(min-width: 525px) 95vw, 70vw"
            className={styles.image}
          />
        </div>
      </div>
    </div>
  );
}
