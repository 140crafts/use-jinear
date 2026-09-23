import React from "react";
import styles from "./CalendarAssigneesList.module.css";
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

interface CalendarAssigneesListProps {
    workspace: WorkspaceDto;
}

const EMPTY_ARRAY: string[] = [];

const CalendarAssigneesList: React.FC<CalendarAssigneesListProps> = ({workspace}) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const setQueryState = useSetQueryState();
    const assigneeIds = useQueryState<string[]>("assigneeIds", queryStateArrayParser) || EMPTY_ARRAY;
    const hasSelection = assigneeIds.length != 0;

    const {data: workspaceMemberListResponse} = useRetrieveWorkspaceMembersQuery({workspaceId: workspace.workspaceId});
    const selectedMembers = workspaceMemberListResponse?.data.content.filter(
        (workspaceMemberDto) => assigneeIds.indexOf(workspaceMemberDto.accountId) != -1
    );

    const onPick = (pickedList: WorkspaceMemberDto[]) => {
        const nextList = pickedList?.map((workspaceMemberDto) => workspaceMemberDto.accountId);
        setQueryState("assigneeIds", queryStateAnyToStringConverter(nextList));
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
                {t("calendarFilterByAssigneeButton")}
            </span>
            <span>
                {hasSelection && `(${assigneeIds.length})`}
            </span>
        </Button>
    );
};

export default CalendarAssigneesList;
