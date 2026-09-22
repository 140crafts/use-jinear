import React from "react";
import styles from "./CalendarWorkflowStatusesList.module.css";
import useTranslation from "@/locales/useTranslation";
import {useAppDispatch} from "@/store";
import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import cn from "classnames";
import type {TeamWorkflowStatusDto, WorkspaceDto} from "@/model/be/jinear-core";
import {popWorkspaceWideWorkflowStatusPickerModal} from "@/store/slice/modalSlice";
import {
    queryStateAnyToStringConverter,
    queryStateArrayParser,
    useQueryState,
    useSetQueryState
} from "@/hooks/useQueryState";

interface CalendarWorkflowStatusesListProps {
    workspace: WorkspaceDto;
}

const EMPTY_ARRAY: string[] = [];

const CalendarWorkflowStatusesList: React.FC<CalendarWorkflowStatusesListProps> = ({workspace}) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const setQueryState = useSetQueryState();
    const workflowStatusIdList = useQueryState<string[]>("workflowStatusIdList", queryStateArrayParser) || EMPTY_ARRAY;
    const hasSelection = workflowStatusIdList.length != 0;

    const onPick = (pickedList: TeamWorkflowStatusDto[]) => {
        const nextList = pickedList?.map((teamWorkflowStatusDto) => teamWorkflowStatusDto.teamWorkflowStatusId);
        setQueryState("workflowStatusIdList", queryStateAnyToStringConverter(nextList));
    };

    const popWorkflowStatusFilterModal = () => {
        dispatch(popWorkspaceWideWorkflowStatusPickerModal({
            workspaceId: workspace.workspaceId,
            visible: true,
            initialSelectionIds: workflowStatusIdList,
            onPick
        }));
    };

    return (
        <Button
            onClick={popWorkflowStatusFilterModal}
            className={cn(styles.filterButton, hasSelection && styles.filterButtonSelected)}
            variant={ButtonVariants.hoverFilled2}
            heightVariant={ButtonHeight.short}
        >
            <span>
                {t("calendarFilterByWorkflowStatusButton")}
            </span>
            <span>
                {hasSelection && `(${workflowStatusIdList.length})`}
            </span>
        </Button>
    );
};

export default CalendarWorkflowStatusesList;
