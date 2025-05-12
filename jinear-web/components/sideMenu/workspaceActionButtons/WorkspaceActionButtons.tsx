import React from "react";
import styles from "./WorkspaceActionButtons.module.css";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { LuClipboardEdit, LuPenSquare, LuRss } from "react-icons/lu";
import ProjectsButton from "@/components/sideMenu/workspaceActionButtons/projectsButton/ProjectsButton";
import useTranslation from "@/locals/useTranslation";
import { popNewTaskModal } from "@/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import { WorkspaceDto } from "@/be/jinear-core";
import { useWorkspaceFirstTeam } from "@/hooks/useWorkspaceFirstTeam";
import TeamsButton from "@/components/sideMenu/workspaceActionButtons/teamsButton/TeamsButton";
import MenuGroupTitle from "@/components/sideMenu/menuGroupTitle/MenuGroupTitle";
import { usePathname } from "next/navigation";
import Logger from "@/utils/logger";

interface WorkspaceActionButtonsProps {
  workspace: WorkspaceDto;
}
const logger = Logger("WorkspaceActionButtons");
const WorkspaceActionButtons: React.FC<WorkspaceActionButtonsProps> = ({ workspace }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const projectsEnabled = true;
  const team = useWorkspaceFirstTeam(workspace.workspaceId);
  const pathname = usePathname();
  const assignedToMePath = `/${workspace?.username}/tasks/assigned-to-me`;
  const lastActivitiesPath = `/${workspace?.username}/tasks/last-activities`;

  const _popNewTaskModal = () => {
    if (workspace && team) {
      dispatch(popNewTaskModal({ visible: true, workspace, team }));
    }
  };

  logger.log({pathname, assignedToMePath, lastActivitiesPath});

  return (
    <div className={styles.container}>
      <MenuGroupTitle label={t("sideMenuYourWorkspaceTitle")} />
      <div className={styles.buttonsContainer}>

        <Button
          heightVariant={ButtonHeight.short}
          variant={ButtonVariants.brandColor}
          className={styles.newTaskButton}
          onClick={_popNewTaskModal}
          disabled={!workspace || !team}
        >
          <LuPenSquare />
          <b>{t("sideMenuNewTask")}</b>
        </Button>

        <Button
          className={styles.button}
          href={lastActivitiesPath}
          variant={pathname?.indexOf(lastActivitiesPath) != -1 ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
        >
          <LuRss className={styles.icon} />
          {t("mainFeaturesMenuLabelLastActivities")}
        </Button>
        <Button
          className={styles.button}
          href={assignedToMePath}
          variant={pathname?.indexOf(assignedToMePath) != -1 ? ButtonVariants.filled2 : ButtonVariants.hoverFilled2}
        >
          <LuClipboardEdit className={styles.icon} />
          {t("mainFeaturesMenuLabelAssignedToMe")}
        </Button>

        {projectsEnabled && workspace && <ProjectsButton workspace={workspace} />}

        <TeamsButton workspace={workspace} />

      </div>
    </div>
  );
};

export default WorkspaceActionButtons;