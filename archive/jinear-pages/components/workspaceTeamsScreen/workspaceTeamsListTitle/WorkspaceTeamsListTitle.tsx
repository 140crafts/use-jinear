import React from "react";
import styles from "./WorkspaceTeamsListTitle.module.css";
import { WorkspaceDto } from "@/be/jinear-core";
import useTranslation from "@/locals/useTranslation";
import Button, { ButtonHeight, ButtonVariants } from "@/components/button";
import { useAppDispatch } from "@/store/store";
import { popNewTeamModal } from "@/slice/modalSlice";

interface WorkspaceTeamsListTitleProps {
  workspace?: WorkspaceDto;
  teamCount?: number;
}

const WorkspaceTeamsListTitle: React.FC<WorkspaceTeamsListTitleProps> = ({ workspace, teamCount }) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();

  const openNewTeamModal = () => {
    dispatch(popNewTeamModal({ visible: true, workspace }));
  };

  return (
    <div className={styles.container}>
      <h2>{t("workspaceTeamListTitle")}{teamCount ? ` (${teamCount})` : ""}</h2>
      <div className={"flex-1"} />
      <Button variant={ButtonVariants.contrast} heightVariant={ButtonHeight.short} onClick={openNewTeamModal}>
        {t("workspaceTeamListNewTeamButton")}
      </Button>
    </div>
  );
};

export default WorkspaceTeamsListTitle;