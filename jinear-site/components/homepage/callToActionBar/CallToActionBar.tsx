import Button, { ButtonVariants } from "@/components/button";
import { APP_URL, SELF_HOSTING_DOCS_URL } from "@/utils/constants";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./CallToActionBar.module.scss";

// Marketing-site version: no auth state. Always offers self-host + cloud paths,
// with cloud actions linking to the jinear-app deployment (NEXT_PUBLIC_APP_URL).
const CallToActionBar: React.FC = () => {
  const { t } = useTranslation();

  return (
    <div className={styles.actionBar}>
      <div className={styles.group}>
        <span className={styles.groupLabel}>{t("homescreenCtaSelfHostLabel")}</span>
        <Button variant={ButtonVariants.outline} href={SELF_HOSTING_DOCS_URL} target={"_blank"}>
          <b>{t("homescreenDeployFree")}</b>
        </Button>
      </div>

      <div className={styles.divider} />

      <div className={styles.group}>
        <span className={styles.groupLabel}>{t("homescreenCtaCloudLabel")}</span>
        <div className={styles.cloudButtons}>
          <Button variant={ButtonVariants.outline} href={`${APP_URL}/register`} target={"_blank"}>
            {t("homescreenTryCloud")}
          </Button>
          <Button variant={ButtonVariants.contrast} href={`${APP_URL}/login`} target={"_blank"} className={styles.loginLink}>
            {t("homescreenLogin")}
          </Button>
        </div>
      </div>
    </div>
  );
};

export default CallToActionBar;
