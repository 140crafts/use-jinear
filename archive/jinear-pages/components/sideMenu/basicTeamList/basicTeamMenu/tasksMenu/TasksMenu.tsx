import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import { popNewTaskModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import useTranslation from "locales/useTranslation";
import { usePathname } from "next/navigation";
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
  const pathname = usePathname();
  const tasksPath = `/${workspace.username}/tasks/${team.username}/tasks`;

  const popNewTask = () => {
    dispatch(popNewTaskModal({ visible: true, workspace, team }));
  };

  return (
    <div className={styles.container}>
      <Button
        className={styles.labelButton}
        variant={tasksPath == pathname ? ButtonVariants.filled : ButtonVariants.hoverFilled2}
        href={`/${workspace.username}/tasks/${team.username}/tasks`}
      >
        <IoList />
        <div>{t("sideMenuTeamActionButtonLabelTasks")}</div>
      </Button>
      <div className={styles.actionButtonsContainer}>
        {team.teamState == "ACTIVE" && (
          <Button
            variant={ButtonVariants.hoverFilled2}
            heightVariant={ButtonHeight.short}
            data-tooltip-right={t("sideMenuNewTask")}
            onClick={popNewTask}
          >
            <IoAdd />
          </Button>
        )}
      </div>
    </div>
  );
};

export default TasksMenu;
