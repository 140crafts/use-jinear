import React from "react";
import styles from "./BottomCta.module.scss";
import useTranslation from "locales/useTranslation";
import Button, { ButtonVariants } from "@/components/button";
import MacTerminal from "@/components/homepage/macTerminal/MacTerminal";

const INSTALL_GUIDE_URL = "https://github.com/140crafts/use-jinear/blob/main/jinear-installation-scripts/README.md";

const BottomCta: React.FC = () => {
  const { t } = useTranslation();

  return (
    <div className={styles.container}>
      <h2 className={styles.title}>{t("landingPageBottomCtaTitle")}</h2>
      <div className={styles.columns}>
        <div className={styles.column}>
          <h3 className={styles.columnTitle}>{t("landingPageBottomCtaDeployTitle")}</h3>
          <p className={styles.columnText}>{t("landingPageBottomCtaDeployText")}</p>
          <MacTerminal command="curl -sSL https://gitlab.com/140crafts/use-jinear/-/raw/main/jinear-installation-scripts/install.sh -o install.sh && chmod +x install.sh && ./install.sh" />
          <Button variant={ButtonVariants.outline} href={INSTALL_GUIDE_URL} target={"_blank"}>
            {t("landingPageBottomCtaDeployBtn")}
          </Button>
        </div>
        <div className={styles.separator} />
        <div className={styles.column}>
          <h3 className={styles.columnTitle}>{t("landingPageBottomCtaCloudTitle")}</h3>
          <p className={styles.columnText}>{t("landingPageBottomCtaCloudText")}</p>
          <Button variant={ButtonVariants.contrast} href={"/register"} className={styles.cloudButton}>
            {t("landingPageBottomCtaCloudBtn")}
          </Button>
        </div>
      </div>
    </div>
  );
};

export default BottomCta;
