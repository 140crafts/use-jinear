import Button, { ButtonVariants } from "@/components/button";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./index.module.css";

interface PricingPageProps {}

const PricingPage: React.FC<PricingPageProps> = ({}) => {
  const { t } = useTranslation();

  return (
    <div className={styles.container}>
      <div className={styles.note}>
        <div className={styles.text}>
          <h1>{t("pricingPageStickerTitle")}</h1>
          <br></br>
          <h2 dangerouslySetInnerHTML={{ __html: t("pricingPageStickerText") }}></h2>
        </div>

        <div className={styles.stickyNote} />
        <div className={styles.stickyNoteShadow} />
        <div className={styles.stickyNoteShadow2} />
      </div>

      <div className={styles.actionContainer}>
        <Button href="/" variant={ButtonVariants.contrast}>
          {t("pricingPageGoBack")}
        </Button>
      </div>
    </div>
  );
};

export default PricingPage;
