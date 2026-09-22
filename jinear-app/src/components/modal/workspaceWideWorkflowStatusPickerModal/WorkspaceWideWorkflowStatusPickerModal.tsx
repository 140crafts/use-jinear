import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import CircularLoading from "@/components/circularLoading/CircularLoading";
import type {TeamWorkflowStatusDto} from "@/model/be/jinear-core";
import {useRetrieveMembershipsQuery} from "@/store/api/teamMemberApi";
import {
    closeWorkspaceWideWorkflowStatusPickerModal,
    selectWorkspaceWideWorkflowStatusPickerModalInitialSelectionIds,
    selectWorkspaceWideWorkflowStatusPickerModalOnPick,
    selectWorkspaceWideWorkflowStatusPickerModalOnlyForTeamIds,
    selectWorkspaceWideWorkflowStatusPickerModalVisible,
    selectWorkspaceWideWorkflowStatusPickerModalWorkspaceId
} from "@/store/slice/modalSlice";
import {useAppDispatch, useTypedSelector} from "@/store";
import useTranslation from "@/locales/useTranslation";
import React, {useCallback, useEffect, useMemo, useState} from "react";
import Modal from "../modal/Modal";
import styles from "./WorkspaceWideWorkflowStatusPickerModal.module.css";
import TeamWorkflowStatusSection from "./teamWorkflowStatusSection/TeamWorkflowStatusSection";

interface WorkspaceWideWorkflowStatusPickerModalProps {
}

const WorkspaceWideWorkflowStatusPickerModal: React.FC<WorkspaceWideWorkflowStatusPickerModalProps> = ({}) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const visible = useTypedSelector(selectWorkspaceWideWorkflowStatusPickerModalVisible);
    const workspaceId = useTypedSelector(selectWorkspaceWideWorkflowStatusPickerModalWorkspaceId);
    const onlyForTeamIds = useTypedSelector(selectWorkspaceWideWorkflowStatusPickerModalOnlyForTeamIds);
    const initialSelectionIds = useTypedSelector(selectWorkspaceWideWorkflowStatusPickerModalInitialSelectionIds);
    const onPick = useTypedSelector(selectWorkspaceWideWorkflowStatusPickerModalOnPick);

    const [selectedIds, setSelectedIds] = useState<string[]>([]);
    // Filled as each team section loads, so confirming can answer with dtos, not ids.
    const [loadedStatuses, setLoadedStatuses] = useState<Record<string, TeamWorkflowStatusDto>>({});

    const {currentData: membershipsResponse, isFetching} = useRetrieveMembershipsQuery(
        {workspaceId: workspaceId || ""},
        {
            skip: workspaceId == null
        }
    );

    // An empty or omitted onlyForTeamIds means every team the account belongs to.
    const teams = useMemo(() => {
        const activeTeams = membershipsResponse?.data
            ?.filter((teamMemberDto) => teamMemberDto?.team?.teamState == "ACTIVE")
            ?.map((teamMemberDto) => teamMemberDto.team)
            ?.filter((team) => team != null) || [];
        if (!onlyForTeamIds || onlyForTeamIds.length == 0) {
            return activeTeams;
        }
        return activeTeams.filter((team) => onlyForTeamIds.indexOf(team.teamId) != -1);
    }, [membershipsResponse, onlyForTeamIds]);

    useEffect(() => {
        if (initialSelectionIds != null && initialSelectionIds.length != 0) {
            setSelectedIds(initialSelectionIds);
        }
    }, [initialSelectionIds]);

    const onStatusesLoaded = useCallback((teamWorkflowStatusList: TeamWorkflowStatusDto[]) => {
        setLoadedStatuses((current) => {
            const missing = teamWorkflowStatusList.filter((status) => current[status.teamWorkflowStatusId] == null);
            if (missing.length == 0) {
                return current;
            }
            const next = {...current};
            missing.forEach((status) => {
                next[status.teamWorkflowStatusId] = status;
            });
            return next;
        });
    }, []);

    const close = () => {
        setSelectedIds([]);
        dispatch(closeWorkspaceWideWorkflowStatusPickerModal());
    };

    const toggleSelected = (teamWorkflowStatusDto: TeamWorkflowStatusDto) => {
        const teamWorkflowStatusId = teamWorkflowStatusDto.teamWorkflowStatusId;
        setSelectedIds((current) =>
            current.indexOf(teamWorkflowStatusId) != -1
                ? current.filter((id) => id != teamWorkflowStatusId)
                : [...current, teamWorkflowStatusId]
        );
    };

    const submitPickedAndClose = () => {
        const pickedList = selectedIds
            .map((teamWorkflowStatusId) => loadedStatuses[teamWorkflowStatusId])
            .filter((status) => status != null);
        onPick?.(pickedList);
        close?.();
    };

    return (
        <Modal
            visible={visible}
            title={t("workspaceWideWorkflowStatusPickerModalTitle")}
            bodyClass={styles.container}
            hasTitleCloseButton={true}
            requestClose={close}
            height={"height-medium-or-full"}
        >
            <div className={styles.list}>
                {teams.map((team) => (
                    <TeamWorkflowStatusSection
                        key={`workspace-wide-workflow-status-team-${team.teamId}`}
                        team={team}
                        selectedIds={selectedIds}
                        onToggle={toggleSelected}
                        onStatusesLoaded={onStatusesLoaded}
                    />
                ))}

                <div className={styles.messageContainer}>
                    {isFetching && membershipsResponse == null && <CircularLoading size={17}/>}
                    {membershipsResponse && teams.length == 0 &&
                        <div>{t("workspaceWideWorkflowStatusPickerModalEmptyState")}</div>}
                </div>
            </div>

            <div className={styles.actionBar}>
                <Button heightVariant={ButtonHeight.short} onClick={close}>
                    {t("workspaceWideWorkflowStatusPickerModalCancelButton")}
                </Button>
                <Button
                    heightVariant={ButtonHeight.short}
                    variant={ButtonVariants.contrast}
                    className={styles.contButton}
                    onClick={submitPickedAndClose}
                >
                    {t("workspaceWideWorkflowStatusPickerModalSelectButton")}
                </Button>
            </div>
        </Modal>
    );
};

export default WorkspaceWideWorkflowStatusPickerModal;
