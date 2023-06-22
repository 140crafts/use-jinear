import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import { popNewTaskBoardModal } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import useTranslation from "locales/useTranslation";
import { useRouter } from "next/router";
import React from "react";
import { IoAdd, IoReaderOutline } from "react-icons/io5";
import styles from "./BoardsMenuTitle.module.css";

interface BoardsMenuTitleProps {
  workspace: WorkspaceDto;
  team: TeamDto;
}

const BoardsMenuTitle: React.FC<BoardsMenuTitleProps> = ({ workspace, team }) => {
  const { t } = useTranslation();
  const router = useRouter();
  const dispatch = useAppDispatch();

  const openNewTaskBoardModal = () => {
    dispatch(popNewTaskBoardModal({ visible: true, workspace, team }));
  };

  const routeToTaskBoardsScreen = () => {
    router.push(`/${workspace.username}/${team.username}/task-boards`);
  };

  return (
    <div className={styles.container}>
      <Button className={styles.labelButton} variant={ButtonVariants.hoverFilled2} onClick={routeToTaskBoardsScreen}>
        <IoReaderOutline />
        <div>{t("sideMenuTeamActionButtonLabelBoards")}</div>
      </Button>
      <div className={styles.actionButtonsContainer}>
        <Button
          variant={ButtonVariants.hoverFilled2}
          heightVariant={ButtonHeight.short}
          onClick={openNewTaskBoardModal}
          data-tooltip-right={t("sideMenuTeamActionButtonLabelBoardsNew")}
        >
          <IoAdd />
        </Button>
      </div>
    </div>
  );
};

export default BoardsMenuTitle;
