import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import type {TeamDto, WorkspaceDto} from "@/model/be/jinear-core";
import {popNewTaskBoardModal} from "@/store/slice/modalSlice";
import {useAppDispatch} from "@/store";
import useTranslation from "@/locales/useTranslation";
import {useLocation, useNavigate} from "react-router-dom";
import React from "react";
import {IoAdd, IoReaderOutline} from "react-icons/io5";
import styles from "./BoardsMenuTitle.module.css";

interface BoardsMenuTitleProps {
  workspace: WorkspaceDto;
  team: TeamDto;
}

const BoardsMenuTitle: React.FC<BoardsMenuTitleProps> = ({ workspace, team }) => {
  const { t } = useTranslation();
  const navigate = useNavigate();
  const {pathname} = useLocation();
  const boardsPath = `/${workspace.username}/tasks/${team.username}/task-boards`;

  const dispatch = useAppDispatch();

  const openNewTaskBoardModal = () => {
    dispatch(popNewTaskBoardModal({ visible: true, workspace, team }));
  };

  const routeToTaskBoardsScreen = () => {
      navigate(`/${workspace.username}/tasks/${team.username}/task-boards`);
  };

  return (
    <div className={styles.container}>
      <Button
        className={styles.labelButton}
        variant={pathname == boardsPath ? ButtonVariants.filled : ButtonVariants.hoverFilled2}
        onClick={routeToTaskBoardsScreen}
      >
        <IoReaderOutline />
        <div>{t("sideMenuTeamActionButtonLabelBoards")}</div>
      </Button>
      <div className={styles.actionButtonsContainer}>
        {team.teamState == "ACTIVE" && (
          <Button
            variant={ButtonVariants.hoverFilled2}
            heightVariant={ButtonHeight.short}
            onClick={openNewTaskBoardModal}
            data-tooltip-right={t("sideMenuTeamActionButtonLabelBoardsNew")}
          >
            <IoAdd />
          </Button>
        )}
      </div>
    </div>
  );
};

export default BoardsMenuTitle;
