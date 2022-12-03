import Button, { ButtonVariants } from "@/components/button";
import MenuGroupTitle from "@/components/sideMenu/menuGroupTitle/MenuGroupTitle";
import useTranslation from "locales/useTranslation";
import React from "react";
import {
  IoArchiveOutline,
  IoCalendarOutline,
  IoList,
  IoTodayOutline,
} from "react-icons/io5";
import styles from "./TeamActionButtons.module.css";

interface TeamActionButtonsProps {}

const TeamActionButtons: React.FC<TeamActionButtonsProps> = ({}) => {
  const { t } = useTranslation();

  return (
    <div className={styles.container}>
      <MenuGroupTitle label={t("sideMenuTeamTasks")} />
      <Button variant={ButtonVariants.hoverFilled2} className={styles.button}>
        <IoTodayOutline />
        <div>{t("sideMenuTeamThisWeek")}</div>
      </Button>
      <Button variant={ButtonVariants.hoverFilled2} className={styles.button}>
        <IoCalendarOutline />
        <div>{t("sideMenuTeamThisMonth")}</div>
      </Button>
      <Button variant={ButtonVariants.hoverFilled2} className={styles.button}>
        <IoList />
        <div>{t("sideMenuTeamBacklog")}</div>
      </Button>
      <Button variant={ButtonVariants.hoverFilled2} className={styles.button}>
        <IoArchiveOutline />
        <div>{t("sideMenuTeamArchive")}</div>
      </Button>
    </div>
  );
};

export default TeamActionButtons;
