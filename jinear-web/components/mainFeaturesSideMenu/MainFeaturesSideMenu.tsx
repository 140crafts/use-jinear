"use client";
import { selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import { usePathname } from "next/navigation";
import React from "react";
import { LuCalendarDays, LuCheckSquare, LuClipboardEdit, LuRss } from "react-icons/lu";
import Button, { ButtonVariants } from "../button";
import styles from "./MainFeaturesSideMenu.module.scss";
import InboxButton from "./inboxButton/InboxButton";

interface MainFeaturesSideMenuProps {}

const MainFeaturesSideMenu: React.FC<MainFeaturesSideMenuProps> = ({}) => {
  const { t } = useTranslation();
  const preferredWorkspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);

  const currentPath = usePathname();
  const calendarPath = `/${preferredWorkspace?.username}/calendar`;
  const tasksPath = `/${preferredWorkspace?.username}/tasks`;
  const inboxPath = `/${preferredWorkspace?.username}/inbox`;
  const assignedToMePath = `/${preferredWorkspace?.username}/assigned-to-me`;
  const lastActivitiesPath = `/${preferredWorkspace?.username}/last-activities`;

  return !preferredWorkspace ? null : (
    // <Transition className={styles.container} initial={true}>
    <div className={styles.container}>
      <InboxButton
        isActive={inboxPath == currentPath}
        workspace={preferredWorkspace}
        buttonStyle={styles.iconButton}
        iconStyle={styles.icon}
      />
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
        variant={calendarPath == currentPath ? ButtonVariants.filled : ButtonVariants.hoverFilled2}
      >
        <LuCalendarDays className={styles.icon} />
        {t("mainFeaturesMenuLabelCalendar")}
      </Button>
      <Button
        className={styles.iconButton}
        href={lastActivitiesPath}
        variant={lastActivitiesPath == currentPath ? ButtonVariants.filled : ButtonVariants.hoverFilled2}
      >
        <LuRss className={styles.icon} />
        {t("mainFeaturesMenuLabelLastActivities")}
      </Button>
      <Button
        className={styles.iconButton}
        href={assignedToMePath}
        variant={assignedToMePath == currentPath ? ButtonVariants.filled : ButtonVariants.hoverFilled2}
      >
        <LuClipboardEdit className={styles.icon} />
        {t("mainFeaturesMenuLabelAssignedToMe")}
      </Button>
    </div>
    // </Transition>
  );
};

export default MainFeaturesSideMenu;
