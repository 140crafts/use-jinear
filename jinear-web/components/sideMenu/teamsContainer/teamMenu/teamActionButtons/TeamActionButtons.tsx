import Button, { ButtonVariants } from "@/components/button";
import MenuGroupTitle from "@/components/sideMenu/menuGroupTitle/MenuGroupTitle";
import useTranslation from "locales/useTranslation";
import { useRouter } from "next/router";
import React from "react";
import {
  IoArchiveOutline,
  IoCalendarOutline,
  IoList,
  IoTodayOutline,
} from "react-icons/io5";
import styles from "./TeamActionButtons.module.css";
interface TeamActionButtonsProps {
  name: string;
  workspaceUsername: string;
}

const TeamActionButtons: React.FC<TeamActionButtonsProps> = ({
  name,
  workspaceUsername,
}) => {
  const { t } = useTranslation();
  const router = useRouter();
  const currentPath = router.asPath;
  const weeklyPath = `/${workspaceUsername}/${name}/weekly`;
  const monthlyPath = `/${workspaceUsername}/${name}/monthly`;
  const backlogPath = `/${workspaceUsername}/${name}/backlog`;
  const archivePath = `/${workspaceUsername}/${name}/archive`;

  return (
    <div className={styles.container}>
      <MenuGroupTitle label={t("sideMenuTeamTasks")} />
      <Button
        href={weeklyPath}
        variant={
          currentPath == weeklyPath
            ? ButtonVariants.filled2
            : ButtonVariants.hoverFilled2
        }
        className={styles.button}
      >
        <IoTodayOutline />
        <div
          className={
            currentPath == weeklyPath ? styles.activeButton : undefined
          }
        >
          {t("sideMenuTeamThisWeek")}
        </div>
      </Button>
      <Button
        href={monthlyPath}
        variant={
          currentPath == monthlyPath
            ? ButtonVariants.filled2
            : ButtonVariants.hoverFilled2
        }
        className={styles.button}
      >
        <IoCalendarOutline />
        <div
          className={
            currentPath == monthlyPath ? styles.activeButton : undefined
          }
        >
          {t("sideMenuTeamThisMonth")}
        </div>
      </Button>
      <Button
        href={backlogPath}
        variant={
          currentPath == backlogPath
            ? ButtonVariants.filled2
            : ButtonVariants.hoverFilled2
        }
        className={styles.button}
      >
        <IoList />
        <div
          className={
            currentPath == backlogPath ? styles.activeButton : undefined
          }
        >
          {t("sideMenuTeamBacklog")}
        </div>
      </Button>
      <Button
        href={archivePath}
        variant={
          currentPath == archivePath
            ? ButtonVariants.filled2
            : ButtonVariants.hoverFilled2
        }
        className={styles.button}
      >
        <IoArchiveOutline />
        <div
          className={
            currentPath == archivePath ? styles.activeButton : undefined
          }
        >
          {t("sideMenuTeamArchive")}
        </div>
      </Button>
    </div>
  );
};

export default TeamActionButtons;
