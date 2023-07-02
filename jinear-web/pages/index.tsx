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
          <h1>Calendar and tasks. No bullshit</h1>
          <span>
            Stop mocking around. You don't need fancy graphs and charts, you need to get shit done. Here's simple calendar and
            your tasks.
          </span>
          <Image alt={""} src={`/images/homescreen/home-${theme}.png`} fill className={styles.image} />
        </div>

        <Line />

        <div className={styles.section}>
          <IoCheckmarkCircleOutline size={ICON_SIZE} />
          <h1>What needs to be done?</h1>
          <span>
            You don't need to fill something like tax return form to plan your next meeting. With little patience you will be able
            to teach your grandma to plan next sprint.
          </span>
          <Image alt={""} src={`/images/homescreen/new-task-${theme}.png`} fill className={styles.image} />
        </div>

        <Line />

        <div className={styles.section}>
          <IoAlarmOutline size={ICON_SIZE} />

          <h1>Remember the milk!</h1>
          <span>
            You keep forgetting tasks? You want us to remind you what to do? Say no more you piece of irresponsible shit. We'll
            spam you with email and notifications.
          </span>
          <Image alt={""} src={`/images/homescreen/reminders-${theme}.png`} fill className={styles.image} />
        </div>

        <Line />

        <div className={styles.section}>
          <IoPeopleOutline size={ICON_SIZE} />
          <h1>Team up</h1>
          <span>Create a collaborative workspace. Close tasks with your team</span>
          <Image alt={""} src={`/images/homescreen/invite-${theme}.png`} fill className={styles.image} />
        </div>

        <Line />

        <div className={styles.section}>
          <IoReaderOutline size={ICON_SIZE} />
          <h1>Set goals with Boards!</h1>
          <span>Focus your team on what work should be done next with deadline.</span>
          <Image alt={""} src={`/images/homescreen/board-${theme}.png`} fill className={styles.image} />
        </div>

        <Line />

        <div className={styles.section}>
          <IoCheckmarkCircleOutline size={ICON_SIZE} />

          <h1>Don't need fancy words</h1>
          <span>
            Create and control your tasks. Link them together, assign somebody, track and subscribe to task activity or add a
            checklist. Simple.
          </span>
          <Image alt={""} src={`/images/homescreen/task-detail-${theme}.png`} fill className={styles.image} />
        </div>
      </div>
    </div>
  );
}
