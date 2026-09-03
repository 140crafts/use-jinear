import type {TeamMemberRoleType, TeamWorkflowStateGroup} from "@/model/be/jinear-core";
import {useRetrieveAllFromTeamQuery} from "@/store/api/teamWorkflowStatusApi";
import useTranslation from "@/locales/useTranslation";
import React from "react";
import SectionTitle from "../../sectionTitle/SectionTitle";
import styles from "./TeamWorkflowSettings.module.css";
import WorkflowGroup from "./workflowGroup/WorkflowGroup";
import CircularLoading from "@/components/circularLoading/CircularLoading.tsx";

interface TeamWorkflowSettingsProps {
    teamId: string;
    teamRole?: TeamMemberRoleType;
}

const WORKFLOW_GROUPS: TeamWorkflowStateGroup[] = ["BACKLOG", "NOT_STARTED", "STARTED", "COMPLETED", "CANCELLED"];

const TeamWorkflowSettings: React.FC<TeamWorkflowSettingsProps> = ({teamId, teamRole}) => {
    const {t} = useTranslation();
    const {data: teamWorkflowListData, isLoading: isTeamWorkflowListLoading} = useRetrieveAllFromTeamQuery(
        {teamId},
        {skip: teamId == null}
    );
    const editable = teamRole == "ADMIN";
    return (
        <div className={styles.container}>
            <SectionTitle
                title={t("teamSettingsScreenWorkflowSectionTitle")}
                description={t("teamSettingsScreenWorkflowSectionDescription")}
            />
            {isTeamWorkflowListLoading && (
                <div className={styles.loadingContainer}>
                    <CircularLoading size={21}/>
                </div>
            )}

            {!isTeamWorkflowListLoading && teamWorkflowListData && (
                <div className={styles.content}>
                    {WORKFLOW_GROUPS.map((groupType) => (
                        <WorkflowGroup
                            key={groupType}
                            teamId={teamId}
                            editable={editable}
                            groupType={groupType}
                            statuses={teamWorkflowListData.data.groupedTeamWorkflowStatuses?.[groupType]}
                        />
                    ))}
                </div>
            )}
        </div>
    );
};

export default TeamWorkflowSettings;
