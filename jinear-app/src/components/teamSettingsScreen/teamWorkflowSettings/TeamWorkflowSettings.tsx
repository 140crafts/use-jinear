import type {TeamMemberRoleType} from "@/model/be/jinear-core";
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
                    <WorkflowGroup
                        editable={editable}
                        groupType={"BACKLOG"}
                        statuses={teamWorkflowListData.data.groupedTeamWorkflowStatuses?.["BACKLOG"]}
                    />
                    <WorkflowGroup
                        editable={editable}
                        groupType={"NOT_STARTED"}
                        statuses={teamWorkflowListData.data.groupedTeamWorkflowStatuses?.["NOT_STARTED"]}
                    />
                    <WorkflowGroup
                        editable={editable}
                        groupType={"STARTED"}
                        statuses={teamWorkflowListData.data.groupedTeamWorkflowStatuses?.["STARTED"]}
                    />
                    <WorkflowGroup
                        editable={editable}
                        groupType={"COMPLETED"}
                        statuses={teamWorkflowListData.data.groupedTeamWorkflowStatuses?.["COMPLETED"]}
                    />
                    <WorkflowGroup
                        editable={editable}
                        groupType={"CANCELLED"}
                        statuses={teamWorkflowListData.data.groupedTeamWorkflowStatuses?.["CANCELLED"]}
                    />
                </div>
            )}
        </div>
    );
};

export default TeamWorkflowSettings;
