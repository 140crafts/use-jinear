import Button from "@/components/button";
import FormLogo from "@/components/formLogo/FormLogo";
import ThemeToggle from "@/components/themeToggle/ThemeToggle";
import useTranslation from "locales/useTranslation";
import React from "react";
import { LuFileText } from "react-icons/lu";
import styles from "./HomePageNavbar.module.scss";

interface HomePageNavbarProps {}

const HomePageNavbar: React.FC<HomePageNavbarProps> = ({}) => {
  const { t } = useTranslation();

  return (
    <div className={styles.header}>
      <FormLogo textClassName={styles.logo} withLeftLine={false} />
      <div className={styles.headerActionBar}>
        <Button href="/pricing">
          <b>{t("homescreenActionBarPricing")}</b>
        </Button>
        <Button href="/terms">
          <b className={styles.termsLabel}>{t("homescreenActionBarTerms")}</b>
          <LuFileText className={styles.termsIcon} />
        </Button>
        <ThemeToggle buttonStyle={styles.themeToggleButton} />
      </div>
    </div>
  );
};

export default HomePageNavbar;
