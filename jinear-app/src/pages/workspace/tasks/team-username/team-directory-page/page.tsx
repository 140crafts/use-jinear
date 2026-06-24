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
import TeamPanel from "@/components/teamPanel/TeamPanel.tsx";
import WorkspaceTeamMemberList
    from "@/components/sideMenu/menuMemberList/workspaceTeamMemberList/WorkspaceTeamMemberList.tsx";
import {useTeamRole} from "@/hooks/useTeamRole.ts";
import Button, {ButtonVariants} from "@/components/button";
import {useWorkspaceRoleIsAdminOrOwner} from "@/hooks/useWorkspaceRoleIsAdminOrOwner.ts";
import {LuHouse} from "react-icons/lu";

interface TeamPageProps {
}

const logger = Logger("TeamPage");

const TeamPage: React.FC<TeamPageProps> = ({}) => {
    const {t} = useTranslation();
    const {workspaceName, teamUsername} = useParams();
    const {workspace, team} = useWorkspaceAndTeamFromName(workspaceName, teamUsername);
    const settingsPath = `/${workspace?.username}/tasks/${team?.username}/settings`;

    const teamRole = useTeamRole({workspaceId: workspace?.workspaceId, teamId: team?.teamId});
    const isWorkspaceAdminOrOwner = useWorkspaceRoleIsAdminOrOwner({workspaceId: workspace?.workspaceId});
    const hasAdminRole = teamRole == 'ADMIN' || isWorkspaceAdminOrOwner;

    return (
        <div className={styles.container}>
            {workspace && team &&
                <div className={styles.header}>
                    <div className={styles.nameContainer}>
                        <LuHouse className={styles.nameIcon} size={21}/>
                        <h1 className={'line-clamp'}>{team.name}</h1>
                    </div>
                    <div className={'flex-1'}/>
                    {hasAdminRole && <Button href={settingsPath}>{t("teamHomeTeamSettings")}</Button>}
                    <WorkspaceTeamMemberList team={team} workspace={workspace}/>
                </div>
            }
            <div className={styles.content}>
                {workspace && team &&
                    <TeamPanel team={team} workspace={workspace}/>
                }
            </div>
        </div>
    );
};

export default TeamPage;
