import { TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./WorkspaceAndTeamInfo.module.css";

interface WorkspaceAndTeamInfoProps {
  workspace: WorkspaceDto;
  team: TeamDto;
}

const WorkspaceAndTeamInfo: React.FC<WorkspaceAndTeamInfoProps> = ({ workspace, team }) => {
  const { t } = useTranslation();
  return workspace.isPersonal ? null : (
    <div className={styles.container}>
      {t("newTaskFormWorkspaceAndTeamInfoLabel")}
      <div>
        <b>{workspace.title + " / " + team.name}</b>
      </div>
    </div>
  );
};

export default WorkspaceAndTeamInfo;
