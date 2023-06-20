import Button, { ButtonVariants } from "@/components/button";
import { TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import useTranslation from "locales/useTranslation";
import React from "react";
import { IoAdd, IoList } from "react-icons/io5";
import styles from "./TasksMenu.module.css";

interface TasksMenuProps {
  workspace: WorkspaceDto;
  team: TeamDto;
}

const TasksMenu: React.FC<TasksMenuProps> = ({ workspace, team }) => {
  const { t } = useTranslation();
  return (
    <div className={styles.container}>
      <Button
        className={styles.labelButton}
        variant={ButtonVariants.hoverFilled2}
        href={`/${workspace.username}/${team.username}/tasks`}
      >
        <IoList />
        <div>{t("sideMenuTeamActionButtonLabelTasks")}</div>
      </Button>
      {!workspace.isPersonal && (
        <div className={styles.actionButtonsContainer}>
          <Button variant={ButtonVariants.hoverFilled2}>
            <IoAdd />
          </Button>
        </div>
      )}
    </div>
  );
};

export default TasksMenu;
