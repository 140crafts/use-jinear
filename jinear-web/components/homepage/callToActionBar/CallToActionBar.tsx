import Button, { ButtonVariants } from "@/components/button";
import CircularLoading from "@/components/circularLoading/CircularLoading";
import { NavigationStatus } from "@/store/slice/accountSlice";
import { ROUTE_IF_LOGGED_IN } from "@/utils/constants";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoArrowForward } from "react-icons/io5";
import styles from "./CallToActionBar.module.scss";
import { useAccountsPreferredWorkspaceIfLoggedIn } from "@/hooks/useAccountsPreferredWorkspaceIfLoggedIn";

interface CallToActionBarProps {
  authState: NavigationStatus;
}

const CallToActionBar: React.FC<CallToActionBarProps> = ({ authState }) => {
  const { t } = useTranslation();
  const preferredWorkspace = useAccountsPreferredWorkspaceIfLoggedIn();
  const preferredRedirectRoute = preferredWorkspace ? `/${preferredWorkspace?.username}` : ROUTE_IF_LOGGED_IN;

  return (
    <div className={styles.actionBar}>
      {authState == "LOGGED_IN" && (
        <Button variant={ButtonVariants.contrast} href={preferredRedirectRoute} className={styles.goToAppButton}>
          <b>{t("homescreenGoToApp")}</b>
          <IoArrowForward />
        </Button>
      )}
      {authState == "NOT_LOGGED_IN" && (
        <>
          <div className={styles.group}>
            <span className={styles.groupLabel}>{t("homescreenCtaSelfHostLabel")}</span>
            <Button variant={ButtonVariants.outline} href={"https://github.com/140crafts/use-jinear/blob/main/jinear-installation-scripts/README.md"} target={"_blank"}>
              <b>{t("homescreenDeployFree")}</b>
            </Button>
          </div>

          <div className={styles.divider} />

          <div className={styles.group}>
            <span className={styles.groupLabel}>{t("homescreenCtaCloudLabel")}</span>
            <div className={styles.cloudButtons}>
              <Button variant={ButtonVariants.outline} href={"/register"}>
                {t("homescreenTryCloud")}
              </Button>
              <Button variant={ButtonVariants.contrast} href={"/login"} className={styles.loginLink}>
                {t("homescreenLogin")}
              </Button>
            </div>
          </div>
        </>
      )}
      {authState == "NOT_DECIDED" && <CircularLoading />}
    </div>
  );
};

export default CallToActionBar;
