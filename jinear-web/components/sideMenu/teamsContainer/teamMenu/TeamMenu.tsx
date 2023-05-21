import { TeamJoinMethodType, TeamVisibilityType } from "@/model/be/jinear-core";
import React from "react";
import styles from "./TeamMenu.module.css";
import TeamActionButtons from "./teamActionButtons/TeamActionButtons";
import TeamMemberList from "./teamMemberList/TeamMemberList";
import TeamTitle from "./teamTitle/TeamTitle";
import TeamTopics from "./teamTopics/TeamTopics";

interface TeamMenuProps {
  teamId: string;
  workspaceId: string;
  name: string;
  username: string;
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
  username,
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
      <TeamActionButtons name={name} username={username} workspaceUsername={workspaceUsername} />
      <TeamTopics teamId={teamId} />
      {/* <TeamThreads /> */}
    </div>
  );
};

export default TeamMenu;
