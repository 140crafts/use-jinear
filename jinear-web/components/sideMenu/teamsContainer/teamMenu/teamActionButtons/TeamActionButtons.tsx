import Button, { ButtonVariants } from "@/components/button";
import MenuGroupTitle from "@/components/sideMenu/menuGroupTitle/MenuGroupTitle";
import useTranslation from "locales/useTranslation";
import { useRouter } from "next/router";
import React from "react";
import { IoArchiveOutline, IoCalendarOutline, IoFolderOutline, IoList, IoPlayOutline, IoTodayOutline } from "react-icons/io5";
import styles from "./TeamActionButtons.module.css";
interface TeamActionButtonsProps {
  name: string;
  workspaceUsername: string;
}

const TeamActionButtons: React.FC<TeamActionButtonsProps> = ({ name, workspaceUsername }) => {
  const { t } = useTranslation();
  const router = useRouter();
  const currentPath = router.asPath;
  const weeklyPath = `/${workspaceUsername}/${name}/weekly`;
  const monthlyPath = `/${workspaceUsername}/${name}/monthly`;
  const taskListPath = `/${workspaceUsername}/${name}/active`;
  const backlogPath = `/${workspaceUsername}/${name}/backlog`;
  const archivePath = `/${workspaceUsername}/${name}/archive`;
  const allPath = `/${workspaceUsername}/${name}/all`;

  return (
    <div className={styles.container}>
      <MenuGroupTitle label={t("sideMenuTeamTimeBased")} />
      <Button
        href={weeklyPath}
        variant={currentPath == weeklyPath ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
        className={styles.button}
      >
        <IoTodayOutline />
        <div className={currentPath == weeklyPath ? styles.activeButton : undefined}>{t("sideMenuTeamThisWeek")}</div>
      </Button>
      <Button
        href={monthlyPath}
        variant={currentPath == monthlyPath ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
        className={styles.button}
      >
        <IoCalendarOutline />
        <div className={currentPath == monthlyPath ? styles.activeButton : undefined}>{t("sideMenuTeamThisMonth")}</div>
      </Button>
      <div className="spacer-h-1" />
      <MenuGroupTitle label={t("sideMenuTeamStatusBased")} />
      <Button
        href={taskListPath}
        variant={currentPath == taskListPath ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
        className={styles.button}
      >
        <IoPlayOutline />
        <div className={currentPath == taskListPath ? styles.activeButton : undefined}>{t("sideMenuTeamActiveTaskList")}</div>
      </Button>
      <Button
        href={backlogPath}
        variant={currentPath == backlogPath ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
        className={styles.button}
      >
        <IoFolderOutline />
        <div className={currentPath == backlogPath ? styles.activeButton : undefined}>{t("sideMenuTeamBacklog")}</div>
      </Button>
      <Button
        href={archivePath}
        variant={currentPath == archivePath ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
        className={styles.button}
      >
        <IoArchiveOutline />
        <div className={currentPath == archivePath ? styles.activeButton : undefined}>{t("sideMenuTeamArchive")}</div>
      </Button>
      <Button
        href={allPath}
        variant={currentPath == allPath ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
        className={styles.button}
      >
        <IoList />
        <div className={currentPath == allPath ? styles.activeButton : undefined}>{t("sideMenuTeamTaskListAll")}</div>
      </Button>
    </div>
  );
};

export default TeamActionButtons;
