import { TeamJoinMethodType, TeamVisibilityType } from "@/model/be/jinear-core";
import React from "react";
import TeamActionButtons from "./teamActionButtons/TeamActionButtons";
import TeamMemberList from "./teamMemberList/TeamMemberList";
import styles from "./TeamMenu.module.css";
import TeamThreads from "./teamThreads/TeamThreads";
import TeamTitle from "./teamTitle/TeamTitle";
import TeamTopics from "./teamTopics/TeamTopics";

interface TeamMenuProps {
  teamId: string;
  workspaceId: string;
  name: string;
  tag: string;
  visibility: TeamVisibilityType;
  joinMethod: TeamJoinMethodType;
  workspaceUsername: string;
  isPersonalWorkspace?: boolean;
}

const TeamMenu: React.FC<TeamMenuProps> = ({
  teamId,
  workspaceId,
  name,
  tag,
  visibility,
  joinMethod,
  workspaceUsername,
  isPersonalWorkspace = false,
}) => {
  return (
    <div className={styles.container}>
      {!isPersonalWorkspace && <div className="spacer-h-1" />}
      {!isPersonalWorkspace && <TeamTitle name={name} />}
      {!isPersonalWorkspace && <TeamMemberList teamId={teamId} />}
      <TeamActionButtons name={name} workspaceUsername={workspaceUsername} />
      <TeamTopics teamId={teamId} />
      <TeamThreads />
    </div>
  );
};

export default TeamMenu;
