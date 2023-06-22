import TeamSettingsScreenBreadcrumb from "@/components/teamSettingsScreen/teamSettingsScreenBreadcrumb/TeamSettingsScreenBreadcrumb";
import TeamWorkflowSettings from "@/components/teamSettingsScreen/teamWorkflowSettings/TeamWorkflowSettings";
import { useRetrieveWorkspaceTeamsQuery } from "@/store/api/teamApi";
import { selectWorkspaceFromWorkspaceUsername } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { useRouter } from "next/router";
import React from "react";
import styles from "./index.module.css";

interface SettingsScreenProps {}

const SettingsScreen: React.FC<SettingsScreenProps> = ({}) => {
  const router = useRouter();
  const workspaceName: string = router.query?.workspaceName as string;
  const teamUsername: string = router.query?.teamUsername as string;

  const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));
  const { data: teamsResponse, isFetching: isTeamsFetching } = useRetrieveWorkspaceTeamsQuery(workspace?.workspaceId || "", {
    skip: workspace == null,
  });
  const team = teamsResponse?.data.find((teamDto) => teamDto.username == teamUsername);

  return (
    <div className={styles.container}>
      {workspace && team && <TeamSettingsScreenBreadcrumb workspace={workspace} team={team} />}
      {team && <TeamWorkflowSettings teamId={team.teamId} />}
    </div>
  );
};

export default SettingsScreen;
