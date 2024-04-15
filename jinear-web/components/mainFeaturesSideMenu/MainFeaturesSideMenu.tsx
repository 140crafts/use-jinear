"use client";
import { AccountsWorkspacePerspectiveDto } from "@/model/be/jinear-core";
import useTranslation from "locales/useTranslation";
import { usePathname } from "next/navigation";
import React from "react";
import { LuCalendarDays, LuCheckSquare, LuClipboardEdit, LuMessagesSquare, LuRss } from "react-icons/lu";
import Button, { ButtonVariants } from "../button";
import styles from "./MainFeaturesSideMenu.module.scss";
import InboxButton from "./inboxButton/InboxButton";

interface MainFeaturesSideMenuProps {
  workspace?: AccountsWorkspacePerspectiveDto;
}

const MainFeaturesSideMenu: React.FC<MainFeaturesSideMenuProps> = ({ workspace }) => {
  const { t } = useTranslation();

  const currentPath = usePathname();
  const calendarPath = `/${workspace?.username}/calendar`;
  const tasksPath = `/${workspace?.username}/tasks`;
  const inboxPath = `/${workspace?.username}/inbox`;
  const assignedToMePath = `/${workspace?.username}/assigned-to-me`;
  const lastActivitiesPath = `/${workspace?.username}/last-activities`;
  const conversationsPath = `/${workspace?.username}/conversations`;

  return !workspace ? null : (
    <div className={styles.container}>
      <InboxButton
        isActive={inboxPath == currentPath}
        workspace={workspace}
        buttonStyle={styles.iconButton}
        iconStyle={styles.icon}
      />
      <Button
        className={styles.iconButton}
        href={conversationsPath}
        variant={currentPath?.indexOf(conversationsPath) != -1 ? ButtonVariants.filled : ButtonVariants.hoverFilled2}
      >
        <LuMessagesSquare className={styles.icon} />
        {t("mainFeaturesMenuLabelConversations")}
      </Button>
      <Button
        className={styles.iconButton}
        href={tasksPath}
        variant={currentPath?.indexOf(tasksPath) != -1 ? ButtonVariants.filled : ButtonVariants.hoverFilled2}
      >
        <LuCheckSquare className={styles.icon} />
        {t("mainFeaturesMenuLabelTasks")}
      </Button>
      <Button
        className={styles.iconButton}
        href={calendarPath}
        variant={currentPath?.indexOf(calendarPath) != -1 ? ButtonVariants.filled : ButtonVariants.hoverFilled2}
      >
        <LuCalendarDays className={styles.icon} />
        {t("mainFeaturesMenuLabelCalendar")}
      </Button>
      <Button
        className={styles.iconButton}
        href={lastActivitiesPath}
        variant={currentPath?.indexOf(lastActivitiesPath) != -1 ? ButtonVariants.filled : ButtonVariants.hoverFilled2}
      >
        <LuRss className={styles.icon} />
        {t("mainFeaturesMenuLabelLastActivities")}
      </Button>
      <Button
        className={styles.iconButton}
        href={assignedToMePath}
        variant={currentPath?.indexOf(assignedToMePath) != -1 ? ButtonVariants.filled : ButtonVariants.hoverFilled2}
      >
        <LuClipboardEdit className={styles.icon} />
        {t("mainFeaturesMenuLabelAssignedToMe")}
      </Button>
    </div>
  );
};

export default MainFeaturesSideMenu;
