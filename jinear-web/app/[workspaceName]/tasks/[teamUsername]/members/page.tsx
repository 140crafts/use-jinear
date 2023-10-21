"use client";
import GenericBreadcrumb from "@/components/genericBreadcrumb/GenericBreadcrumb";
import TeamMemberList from "@/components/teamMembersScreen/teamMemberList/TeamMemberList";
import TeamMembersScreenHeader from "@/components/teamMembersScreen/teamMembersScreenHeader/TeamMembersScreenHeader";
import { useRetrieveWorkspaceTeamsQuery } from "@/store/api/teamApi";
import { selectWorkspaceFromWorkspaceUsername } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import { useParams } from "next/navigation";
import React from "react";
import styles from "./index.module.css";

interface TeamMembersScreenProps {}

const TeamMembersScreen: React.FC<TeamMembersScreenProps> = ({}) => {
  const { t } = useTranslation();
  const params = useParams();
  const workspaceName: string = params?.workspaceName as string;
  const teamUsername: string = params?.teamUsername as string;

  const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));
  const { data: teamsResponse, isFetching: isTeamsFetching } = useRetrieveWorkspaceTeamsQuery(workspace?.workspaceId || "", {
    skip: workspace == null,
  });
  const team = teamsResponse?.data.find((teamDto) => teamDto.username == teamUsername);

  return (
    <div className={styles.container}>
      {workspace && team && (
        <GenericBreadcrumb
          workspace={workspace}
          team={team}
          label={t("teamMemberScreenBreadcrumbTitle")}
          pathAfterWorkspaceAndTeam="/members"
        />
      )}
      {workspace && team && <TeamMembersScreenHeader workspace={workspace} team={team} />}
      {team && <TeamMemberList teamId={team.teamId} />}
    </div>
  );
};

export default TeamMembersScreen;
