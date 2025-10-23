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
          <Button variant={ButtonVariants.contrast} href={"/login"} className={styles.loginButton}>
            <b>{t("homescreenLogin")}</b>
          </Button>
          <Button variant={ButtonVariants.outline} href={"/register"}>
            {t("homescreenRegister")}
          </Button>
        </>
      )}
      {authState == "NOT_DECIDED" && <CircularLoading />}
    </div>
  );
};

export default CallToActionBar;
