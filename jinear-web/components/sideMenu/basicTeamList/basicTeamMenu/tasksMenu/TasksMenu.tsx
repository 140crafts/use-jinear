import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import { popNewTaskModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
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
  const dispatch = useAppDispatch();

  const popNewTask = () => {
    dispatch(popNewTaskModal({ visible: true, workspace, team }));
  };

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
          <Button
            variant={ButtonVariants.hoverFilled2}
            heightVariant={ButtonHeight.short}
            data-tooltip-right={t("sideMenuNewTask")}
            onClick={popNewTask}
          >
            <IoAdd />
          </Button>
        </div>
      )}
    </div>
  );
};

export default TasksMenu;
