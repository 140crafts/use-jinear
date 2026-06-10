import React, {useState} from 'react';
import styles from './CalendarBoardsList.module.css';
import useTranslation from "@/locals/useTranslation";
import {useAppDispatch} from "@/store/store";
import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import {TaskBoardDto, WorkspaceDto} from "@/be/jinear-core";
import {popBoardPickerModal} from "@/slice/modalSlice";
import {
    queryStateAnyToStringConverter,
    queryStateArrayParser,
    useQueryState,
    useSetQueryState
} from "@/hooks/useQueryState";
import MenuGroupTitle from "@/components/sideMenu/menuGroupTitle/MenuGroupTitle";
import {IoShareOutline} from "react-icons/io5";

interface CalendarBoardsListProps {
    workspace: WorkspaceDto;
}

const CalendarBoardsList: React.FC<CalendarBoardsListProps> = ({workspace}) => {
    const {t} = useTranslation();
    const dispatch = useAppDispatch();
    const setQueryState = useSetQueryState();
    const taskBoards = useQueryState<string[]>("taskBoards", queryStateArrayParser) || [];
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
        <div className={styles.container}>
            <div className="spacer-h-1"/>
            <div className={styles.titleContainer}>
                <MenuGroupTitle label={t("calendarFilterByPropertyLabel")} hasAddButton={false}/>
            </div>

            <Button onClick={popBoardFilterModal} className={styles.filterButton}>
                <span>
                    {t('calendarFilterByBoardButton')}
                </span>
                <span>
                    {taskBoards.length != 0 && <b>({taskBoards.length})</b>}
                </span>
            </Button>
        </div>
    );
}

export default CalendarBoardsList;