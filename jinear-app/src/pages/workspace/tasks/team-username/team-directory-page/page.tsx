import CircularLoading from "@/components/circularLoading/CircularLoading";
import TeamLastActivities from "@/components/teamLastActivities/TeamLastActivities";
import TeamLongLastingTasks from "@/components/teamLongLastingTasks/TeamLongLastingTasks";
import TeamNumbers from "@/components/teamNumberCards/TeamNumbers";
import {useWorkspaceAndTeamFromName} from "@/hooks/useWorkspaceAndTeamFromName";
import Logger from "@/util/logger";
import useTranslation from "@/locales/useTranslation";
import React from "react";
import styles from "./index.module.scss";
import {useParams} from "react-router-dom";

interface TeamPageProps {
}

const logger = Logger("TeamPage");

const TeamPage: React.FC<TeamPageProps> = ({}) => {
    const {t} = useTranslation();
    const {workspaceName, teamUsername} = useParams();
    const {workspace, team} = useWorkspaceAndTeamFromName(workspaceName, teamUsername);

    return (
        <div className={styles.container}>
            <div className={styles.header}>{team &&
                <h1>{t("teamHomePageTitle").replace("${teamName}", team?.name)}</h1>}</div>
            <div className={styles.content}>
                {workspace && team ? (
                    <>
                        <TeamNumbers className={styles.numbers} workspace={workspace} team={team}/>
                        <TeamLastActivities className={styles.activities} teamId={team.teamId}
                                            workspaceId={workspace.workspaceId}/>
                        <TeamLongLastingTasks className={styles.tasks} teamId={team.teamId}
                                              workspaceId={workspace.workspaceId}/>
                    </>
                ) : (
                    <CircularLoading/>
                )}
            </div>
        </div>
    );
};

export default TeamPage;
