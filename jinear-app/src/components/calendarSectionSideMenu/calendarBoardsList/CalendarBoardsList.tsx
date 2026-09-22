import React, {useState} from 'react';
import styles from './CalendarBoardsList.module.css';
import useTranslation from "@/locals/useTranslation";
import {useAppDispatch} from "@/store";
import Button from "@/components/button";
import cn from "classnames";
import type {TaskBoardDto, WorkspaceDto} from "@/be/jinear-core";
import {popBoardPickerModal} from "@/slice/modalSlice";
import {
    queryStateAnyToStringConverter,
    queryStateArrayParser,
    useQueryState,
    useSetQueryState
} from "@/hooks/useQueryState";

interface CalendarBoardsListProps {
    workspace: WorkspaceDto;
}

const CalendarBoardsList: React.FC<CalendarBoardsListProps> = ({workspace}) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const setQueryState = useSetQueryState();
    const taskBoards = useQueryState<string[]>("taskBoards", queryStateArrayParser) || [];
    const hasSelection = taskBoards.length != 0;
    const [selectedBoards, setSelectedBoards] = useState<TaskBoardDto[]>([]);

    const popBoardFilterModal = () => {
        dispatch(popBoardPickerModal({
            workspaceId: workspace.workspaceId,
            visible: true,
            onPick: onBoardPick,
            multiple: true,
            initialSelectionOnMultiple: selectedBoards
        }));
    }

    const onBoardPick = (pickedList: TaskBoardDto[]) => {
        const nextList = pickedList?.map(board => board.taskBoardId);
        setQueryState("taskBoards", queryStateAnyToStringConverter(nextList));
        setSelectedBoards(pickedList);
    }

    return (
        <Button
            onClick={popBoardFilterModal}
            className={cn(styles.filterButton, hasSelection && styles.filterButtonSelected)}
        >
            <span>
                {t('calendarFilterByBoardButton')}
            </span>
            <span>
                {hasSelection && `(${taskBoards.length})`}
            </span>
        </Button>
    );
}

export default CalendarBoardsList;