import {useRetrieveAllFromTeamQuery} from "@/store/api/teamWorkflowStatusApi";
import Logger from "@/util/logger";
import React from "react";
import StatusListButton from "./statusListButton/StatusListButton";

import styles from "./TeamWorkflowStatusList.module.css";
import CircularLoading from "@/components/circularLoading/CircularLoading.tsx";

interface TeamWorkflowStatusListProps {
    teamId: string;
}

const logger = Logger("TeamWorkflowStatusList");

const TeamWorkflowStatusList: React.FC<TeamWorkflowStatusListProps> = ({teamId}) => {
    const {
        data: teamWorkflowStatusResponse,
        isFetching: isTeamWorkflowStatusListFetching
    } = useRetrieveAllFromTeamQuery({
        teamId,
    });

    return (
        <div className={styles.container}>
            {isTeamWorkflowStatusListFetching ? (
                <div className={styles.loadingContainer}>
                    <CircularLoading size={21}/>
                </div>
            ) : (
                <>
                    {teamWorkflowStatusResponse?.data.groupedTeamWorkflowStatuses.BACKLOG?.map((wfs) => (
                        <StatusListButton key={wfs.workflowStateGroup} wfs={wfs}/>
                    ))}
                    {teamWorkflowStatusResponse?.data.groupedTeamWorkflowStatuses.NOT_STARTED?.map((wfs) => (
                        <StatusListButton key={wfs.workflowStateGroup} wfs={wfs}/>
                    ))}
                    {teamWorkflowStatusResponse?.data.groupedTeamWorkflowStatuses.STARTED?.map((wfs) => (
                        <StatusListButton key={wfs.workflowStateGroup} wfs={wfs}/>
                    ))}
                    {teamWorkflowStatusResponse?.data.groupedTeamWorkflowStatuses.COMPLETED?.map((wfs) => (
                        <StatusListButton key={wfs.workflowStateGroup} wfs={wfs}/>
                    ))}
                    {teamWorkflowStatusResponse?.data.groupedTeamWorkflowStatuses.CANCELLED?.map((wfs) => (
                        <StatusListButton key={wfs.workflowStateGroup} wfs={wfs}/>
                    ))}
                </>
            )}
        </div>
    );
};

export default TeamWorkflowStatusList;
