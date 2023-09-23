import { TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import { hasWorkspaceFilePermissions } from "@/utils/permissionHelper";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./BasicTeamMenu.module.css";
import BoardsMenu from "./boardsMenu/BoardsMenu";
import FilesMenu from "./filesMenu/FilesMenu";
import TasksMenu from "./tasksMenu/TasksMenu";
import TeamTitle from "./teamTitle/TeamTitle";
import TopicsMenu from "./topicsMenu/TopicsMenu";

interface BasicTeamMenuProps {
  workspace: WorkspaceDto;
  team: TeamDto;
}

const BasicTeamMenu: React.FC<BasicTeamMenuProps> = ({ workspace, team }) => {
  const { t } = useTranslation();

  return (
    <div className={styles.container}>
      <TeamTitle workspace={workspace} team={team} />
      <div className="spacer-h-1" />
      <div className={styles.teamActionButtonContainer}>
        <TasksMenu workspace={workspace} team={team} />
        <BoardsMenu workspace={workspace} team={team} />
        <TopicsMenu workspace={workspace} team={team} />
        {hasWorkspaceFilePermissions(workspace) && <FilesMenu workspace={workspace} team={team} />}
      </div>
    </div>
  );
};

export default BasicTeamMenu;
