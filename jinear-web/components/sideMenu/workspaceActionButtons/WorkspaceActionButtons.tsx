import React from "react";
import styles from "./WorkspaceActionButtons.module.css";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { LuPen } from "react-icons/lu";
import ProjectsButton from "@/components/sideMenu/workspaceActionButtons/projectsButton/ProjectsButton";
import { useFeatureFlag } from "@/hooks/useFeatureFlag";
import useTranslation from "@/locals/useTranslation";
import { popNewTaskModal } from "@/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import { WorkspaceDto } from "@/be/jinear-core";
import { useWorkspaceFirstTeam } from "@/hooks/useWorkspaceFirstTeam";
import TeamsButton from "@/components/sideMenu/workspaceActionButtons/teamsButton/TeamsButton";
import MenuGroupTitle from "@/components/sideMenu/menuGroupTitle/MenuGroupTitle";

interface WorkspaceActionButtonsProps {
  workspace: WorkspaceDto;
}

const WorkspaceActionButtons: React.FC<WorkspaceActionButtonsProps> = ({ workspace }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const projectsEnabled = useFeatureFlag("PROJECTS");
  const team = useWorkspaceFirstTeam(workspace.workspaceId);

  const _popNewTaskModal = () => {
    if (workspace) {
      dispatch(popNewTaskModal({ visible: true, workspace, team }));
    }
  };

  return (
    <div className={styles.container}>
      <MenuGroupTitle label={t("sideMenuYourWorkspaceTitle")} />
      <div className={styles.buttonsContainer}>
        <Button
          heightVariant={ButtonHeight.short}
          variant={ButtonVariants.filled2}
          className={styles.newTaskButton}
          onClick={_popNewTaskModal}
        >
          <LuPen />
          <b>{t("sideMenuNewTask")}</b>
        </Button>
        {projectsEnabled && workspace && <ProjectsButton workspace={workspace} />}
        <TeamsButton workspace={workspace} />
      </div>
    </div>
  );
};

export default WorkspaceActionButtons;