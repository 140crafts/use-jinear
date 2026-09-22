import Button, {ButtonVariants} from "@/components/button";
import CircularLoading from "@/components/circularLoading/CircularLoading";
import type {TeamDto, TeamWorkflowStatusDto} from "@/model/be/jinear-core";
import {useRetrieveAllFromTeamQuery} from "@/store/api/teamWorkflowStatusApi";
import cn from "classnames";
import useTranslation from "@/locales/useTranslation";
import React, {useEffect, useMemo} from "react";
import {IoCheckmark} from "react-icons/io5";
import {retrieveTaskStatusIcon} from "@/util/taskIconFactory";
import styles from "./TeamWorkflowStatusSection.module.css";

interface TeamWorkflowStatusSectionProps {
    team: TeamDto;
    selectedIds: string[];
    onToggle: (teamWorkflowStatusDto: TeamWorkflowStatusDto) => void;
    onStatusesLoaded: (teamWorkflowStatusList: TeamWorkflowStatusDto[]) => void;
}

const GROUP_ORDER = ["BACKLOG", "NOT_STARTED", "STARTED", "COMPLETED", "CANCELLED"] as const;
const STATUS_ICON_SIZE = 15;

// Statuses live per team, so each section owns its own query. That is also why this is a
// component and not a loop in the parent: hooks cannot be called per team in one render.
const TeamWorkflowStatusSection: React.FC<TeamWorkflowStatusSectionProps> = ({team, selectedIds, onToggle, onStatusesLoaded}) => {
    const {t} = useTranslation();

    const {currentData: teamWorkflowStatusListResponse, isFetching} = useRetrieveAllFromTeamQuery({teamId: team.teamId});

    const statuses = useMemo(() => {
        const grouped = teamWorkflowStatusListResponse?.data.groupedTeamWorkflowStatuses;
        if (!grouped) {
            return [];
        }
        return GROUP_ORDER.flatMap((group) => grouped[group] || []);
    }, [teamWorkflowStatusListResponse]);

    // The parent keeps ids, so it needs the dtos this section loaded to answer with them.
    useEffect(() => {
        if (statuses.length != 0) {
            onStatusesLoaded(statuses);
        }
    }, [statuses, onStatusesLoaded]);

    return (
        <div className={styles.container}>
            <div className={styles.teamTitle}>{team.name}</div>

            {statuses.map((teamWorkflowStatusDto) => {
                const isSelected = selectedIds.indexOf(teamWorkflowStatusDto.teamWorkflowStatusId) != -1;
                const StatusIcon = retrieveTaskStatusIcon(teamWorkflowStatusDto.workflowStateGroup);
                return (
                    <Button
                        key={`workspace-wide-workflow-status-${teamWorkflowStatusDto.teamWorkflowStatusId}`}
                        variant={ButtonVariants.default}
                        className={cn(styles.listItemButton, isSelected && styles.listItemButtonSelected)}
                        onClick={() => onToggle(teamWorkflowStatusDto)}
                    >
                        <div className={styles.checkContainer}>{isSelected && <IoCheckmark/>}</div>
                        <StatusIcon size={STATUS_ICON_SIZE}/>
                        {teamWorkflowStatusDto.name}
                    </Button>
                );
            })}

            <div className={styles.messageContainer}>
                {isFetching && teamWorkflowStatusListResponse == null && <CircularLoading size={17}/>}
                {teamWorkflowStatusListResponse && statuses.length == 0 && (
                    <div>{t("teamWorkflowStatusPickerModalEmptyState")}</div>
                )}
            </div>
        </div>
    );
};

export default TeamWorkflowStatusSection;
