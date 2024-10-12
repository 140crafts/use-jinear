"use client";
import Line from "@/components/line/Line";
import TeamStateSettings from "@/components/teamSettingsScreen/teamStateSettings/TeamStateSettings";
import TeamTaskVisibilityTypeSettings
  from "@/components/teamSettingsScreen/teamTaskVisibilityTypeSettings/TeamTaskVisibilityTypeSettings";
import TeamWorkflowSettings from "@/components/teamSettingsScreen/teamWorkflowSettings/TeamWorkflowSettings";
import { useTeamRole } from "@/hooks/useTeamRole";
import { useRetrieveWorkspaceTeamsQuery } from "@/store/api/teamApi";
import { selectWorkspaceFromWorkspaceUsername } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { useParams } from "next/navigation";
import React from "react";
import styles from "./index.module.css";
import { useWorkspaceOwnerOrAdminOrTeamAdmin } from "@/hooks/useWorkspaceOwnerOrAdminOrTeamAdmin";

interface SettingsScreenProps {
}

const SettingsScreen: React.FC<SettingsScreenProps> = ({}) => {
  const params = useParams();
  const workspaceName: string = params?.workspaceName as string;
  const teamUsername: string = params?.teamUsername as string;

  const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));
  const {
    data: teamsResponse,
    isFetching: isTeamsFetching
  } = useRetrieveWorkspaceTeamsQuery(workspace?.workspaceId || "", {
    skip: workspace == null
  });
  const team = teamsResponse?.data.find((teamDto) => teamDto.username == teamUsername);
  const teamRole = useTeamRole({ workspaceId: workspace?.workspaceId, teamId: team?.teamId });

  const accountHasEditRole = useWorkspaceOwnerOrAdminOrTeamAdmin({
    workspaceId: team?.workspaceId,
    teamId: team?.teamId
  });

  return (
    <div className={styles.container}>
      {team && workspace && (
        <>
          <TeamTaskVisibilityTypeSettings team={team} workspace={workspace} accountHasEditRole={accountHasEditRole}/>
          <Line />
        </>
      )}

      {team && (
        <>
          <TeamWorkflowSettings teamId={team.teamId} teamRole={teamRole} />
          <Line />
        </>
      )}
      {team && teamRole == "ADMIN" && (
        <>
          <TeamStateSettings team={team} />
        </>
      )}
    </div>
  );
};

export default SettingsScreen;
