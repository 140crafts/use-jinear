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
      <MenuGroupTitle label="Tasks" />
      <Button variant={ButtonVariants.hoverFilled2} className={styles.button}>
        <IoTodayOutline />
        <div>This Week</div>
      </Button>
      <Button variant={ButtonVariants.hoverFilled2} className={styles.button}>
        <IoCalendarOutline />
        <div>This Month</div>
      </Button>
      <Button variant={ButtonVariants.hoverFilled2} className={styles.button}>
        <IoList />
        <div>Backlog</div>
      </Button>
      <Button variant={ButtonVariants.hoverFilled2} className={styles.button}>
        <IoArchiveOutline />
        <div>Archive</div>
      </Button>
    </div>
  );
};

export default TeamActionButtons;
