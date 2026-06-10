"use client";
import CircularLoading from "@/components/circularLoading/CircularLoading";
import TeamLastActivities from "@/components/teamLastActivities/TeamLastActivities";
import TeamLongLastingTasks from "@/components/teamLongLastingTasks/TeamLongLastingTasks";
import TeamNumbers from "@/components/teamNumberCards/TeamNumbers";
import { useWorkspaceAndTeamFromName } from "@/hooks/useWorkspaceAndTeamFromName";
import Logger from "@/utils/logger";
import useTranslation from "locales/useTranslation";
import { useParams } from "next/navigation";
import React from "react";
import styles from "./index.module.scss";

interface TeamPageProps {}

const logger = Logger("TeamPage");

const TeamPage: React.FC<TeamPageProps> = ({}) => {
  const { t } = useTranslation();
  const params = useParams();
  const workspaceName: string = params?.workspaceName as string;
  const teamUsername: string = params?.teamUsername as string;
  const { workspace, team } = useWorkspaceAndTeamFromName(workspaceName, teamUsername);

  return (
    <div className={styles.container}>
      <div className={styles.header}>{team && <h1>{t("teamHomePageTitle").replace("${teamName}", team?.name)}</h1>}</div>
      <div className={styles.content}>
        {workspace && team ? (
          <>
            <TeamNumbers className={styles.numbers} workspace={workspace} team={team} />
            <TeamLastActivities className={styles.activities} teamId={team.teamId} workspaceId={workspace.workspaceId} />
            <TeamLongLastingTasks className={styles.tasks} teamId={team.teamId} workspaceId={workspace.workspaceId} />
          </>
        ) : (
          <CircularLoading />
        )}
      </div>
    </div>
  );
};

export default TeamPage;
