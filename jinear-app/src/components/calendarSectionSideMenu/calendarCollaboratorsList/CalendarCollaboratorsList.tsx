import React from "react";
import styles from "./CalendarCollaboratorsList.module.css";
import useTranslation from "@/locales/useTranslation";
import {useAppDispatch} from "@/store";
import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import cn from "classnames";
import type {WorkspaceDto, WorkspaceMemberDto} from "@/model/be/jinear-core";
import {popWorkspaceMemberPickerModal} from "@/store/slice/modalSlice";
import {useRetrieveWorkspaceMembersQuery} from "@/store/api/workspaceMemberApi";
import {
    queryStateAnyToStringConverter,
    queryStateArrayParser,
    useQueryState,
    useSetQueryState
} from "@/hooks/useQueryState";

interface CalendarCollaboratorsListProps {
    workspace: WorkspaceDto;
}

const EMPTY_ARRAY: string[] = [];

const CalendarCollaboratorsList: React.FC<CalendarCollaboratorsListProps> = ({workspace}) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const setQueryState = useSetQueryState();
    const collaboratorIds = useQueryState<string[]>("collaboratorIds", queryStateArrayParser) || EMPTY_ARRAY;
    const hasSelection = collaboratorIds.length != 0;

    const {data: workspaceMemberListResponse} = useRetrieveWorkspaceMembersQuery({workspaceId: workspace.workspaceId});
    const selectedMembers = workspaceMemberListResponse?.data.content.filter(
        (workspaceMemberDto) => collaboratorIds.indexOf(workspaceMemberDto.accountId) != -1
    );

    const onPick = (pickedList: WorkspaceMemberDto[]) => {
        const nextList = pickedList?.map((workspaceMemberDto) => workspaceMemberDto.accountId);
        setQueryState("collaboratorIds", queryStateAnyToStringConverter(nextList));
    };

    const popMemberFilterModal = () => {
        dispatch(popWorkspaceMemberPickerModal({
            workspaceId: workspace.workspaceId,
            visible: true,
            onPick,
            multiple: true,
            initialSelectionOnMultiple: selectedMembers
        }));
    };

    return (
        <Button
            onClick={popMemberFilterModal}
            className={cn(styles.filterButton, hasSelection && styles.filterButtonSelected)}
            variant={ButtonVariants.hoverFilled2}
            heightVariant={ButtonHeight.short}
        >
            <span>
                {t("calendarFilterByCollaboratorButton")}
            </span>
            <span>
                {hasSelection && `(${collaboratorIds.length})`}
            </span>
        </Button>
    );
};

export default CalendarCollaboratorsList;
