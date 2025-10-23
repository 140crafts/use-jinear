import Button, { ButtonVariants } from "@/components/button";
import FormLogo from "@/components/formLogo/FormLogo";
import ThemeToggle from "@/components/themeToggle/ThemeToggle";
import useTranslation from "locales/useTranslation";
import React from "react";
import { LuCircleDollarSign, LuFileText, LuGithub, LuGitlab } from "react-icons/lu";
import styles from "./HomePageNavbar.module.scss";
import { FaGithub, FaGitlab } from "react-icons/fa6";

interface HomePageNavbarProps {
}

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

        <Button href="https://github.com/140crafts/use-jinear" target={"_blank"} className={styles.button}>
          <LuGithub />
          <b className={styles.termsLabel}>GitHub</b>
        </Button>
        <Button href="https://gitlab.com/140crafts/use-jinear" target={"_blank"} className={styles.button}>
          <LuGitlab />
          <b className={styles.termsLabel}>GitLab</b>
        </Button>

        <ThemeToggle buttonStyle={styles.themeToggleButton} />
      </div>
    </div>
  );
};

export default HomePageNavbar;
