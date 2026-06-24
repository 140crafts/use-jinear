import type {TaskBoardDto, TaskDto, TeamDto, WorkspaceDto} from "@/be/jinear-core";
import {useInitializeTaskBoardEntryMutation} from "@/api/taskBoardEntryApi";

import Button, {ButtonHeight, ButtonVariants} from "@/components/button";
import {useCurrentAccountsTeamRoleIsAdmin} from "@/hooks/useCurrentAccountsTeamRole";
import {
    useUpdateColorMutation,
    useUpdateDueDateMutation,
    useUpdateStateMutation,
    useUpdateTitleMutation
} from "@/api/taskBoardApi";
import {
    changeLoadingModalVisibility,
    closeBasicTextInputModal,
    closeDatePickerModal,
    closeSearchTaskModal,
    popBasicTextInputModal,
    popDatePickerModal,
    popNewTaskModal,
    popSearchTaskModal
} from "@/slice/modalSlice";
import {useAppDispatch} from "@/store";
import cn from "classnames";
import {differenceInDays, format, isToday, startOfToday} from "date-fns";
import useTranslation from "@/locals/useTranslation";
import React, {type ChangeEvent, useEffect, useMemo, useRef, useState} from "react";
import {IoList, IoLockClosed, IoLockClosedOutline, IoPencil, IoStatsChart} from "react-icons/io5";
import styles from "./TaskBoardTitle.module.scss";
import {useSetQueryState} from "@/hooks/useQueryState";
import {LuCalendarClock, LuCircle, LuSearch, LuSquarePen} from "react-icons/lu";
import TaskBoardQuickFilterBar
    from "@/components/taskLists/taskBoardList/taskBoard/taskBoardQuickFilterBar/TaskBoardQuickFilterBar";
import getCssVariable from "@/util/cssHelper";
import {useDebouncedEffect} from "@/hooks/useDebouncedEffect";
import {setTaskBoardDefaultDisplayFormat} from "@/components/taskLists/taskBoardList/taskBoard/TaskBoard";
import Line from "@/components/line/Line";

interface TaskBoardTitleProps {
    taskBoard: TaskBoardDto;
    team: TeamDto;
    workspace: WorkspaceDto;
    displayFormat: "list" | "column";
    onViewTypeChange: (nextViewType: "list" | "column") => void;
}

const TaskBoardTitle: React.FC<TaskBoardTitleProps> = ({
                                                           taskBoard,
                                                           team,
                                                           workspace,
                                                           displayFormat,
                                                           onViewTypeChange
                                                       }) => {
    const {t} = useTranslation();
    const setQueryState = useSetQueryState();
    const dispatch = useAppDispatch();
    const colorInputRef = useRef<HTMLInputElement>(null);

    const title = taskBoard?.title;
    const taskBoardId = taskBoard?.taskBoardId;
    const boardState = taskBoard?.state;
    const dueDate = taskBoard?.dueDate;
    const boardLink = `/${workspace?.username || ""}/tasks/${team?.username || ""}/task-boards/${taskBoardId}`;

    const isTeamAdmin = useCurrentAccountsTeamRoleIsAdmin({workspaceId: workspace.workspaceId, teamId: team.teamId});
    const [initializeTaskBoardEntry, {
        isLoading: isInitializeLoading,
        isSuccess: isInitializeSuccess
    }] = useInitializeTaskBoardEntryMutation();
    const [updateState, {isLoading: isUpdateStateLoading}] = useUpdateStateMutation();
    const [updateDueDate, {isLoading: isUpdateDueDateLoading}] = useUpdateDueDateMutation();
    const [updateTitle, {isLoading: isUpdateTitleLoading}] = useUpdateTitleMutation();
    const [updateColor, {isLoading: isUpdateColorLoading}] = useUpdateColorMutation();

    const [boardColor, setBoardColor] = useState<string | null | undefined>(taskBoard?.color);

    useDebouncedEffect(() => boardColor && updateColor({taskBoardId, color: boardColor}), [boardColor], 500);

    useEffect(() => {
        dispatch(
            changeLoadingModalVisibility({
                visible: isInitializeLoading || isUpdateStateLoading || isUpdateDueDateLoading || isUpdateTitleLoading
            })
        );
        if (!isInitializeLoading) {
            dispatch(closeSearchTaskModal());
        }
    }, [isInitializeLoading, isUpdateStateLoading, isUpdateDueDateLoading, isUpdateTitleLoading]);

    const openSearchTaskModal = () => {
        dispatch(
            popSearchTaskModal({
                workspaceId: workspace.workspaceId,
                teamIds: [team.teamId],
                onSelect: onExistingTaskSelect,
                visible: true
            })
        );
    };

    const openNewTaskModal = () => {
        dispatch(popNewTaskModal({
            visible: true,
            workspace: workspace,
            team: team,
            initialBoard: taskBoard
        }));
    };

    const onExistingTaskSelect = (selectedTask: TaskDto) => {
        dispatch(changeLoadingModalVisibility({visible: true}));
        initializeTaskBoardEntry({
            taskBoardId,
            taskId: selectedTask.taskId
        });
    };

    const toggleBoardState = () => {
        const state = boardState == "OPEN" ? "CLOSED" : "OPEN";
        updateState({taskBoardId, state});
    };

    const changeDueDate = (dueDate: Date | null) => {
        dispatch(closeDatePickerModal());
        updateDueDate({taskBoardId, dueDate});
    };

    const changeTitle = (title: string) => {
        dispatch(closeBasicTextInputModal());
        updateTitle({taskBoardId, title});
    };

    const popDatePickerForDueDateUpdate = () => {
        dispatch(
            popDatePickerModal({
                visible: true,
                onDateChange: changeDueDate
            })
        );
    };

    const popTitleChangeModal = () => {
        dispatch(
            popBasicTextInputModal({
                visible: true,
                title: t("taskBoardChangeTitleModalTitle"),
                infoText: t("taskBoardChangeTitleModalInfoText"),
                initialText: title,
                onSubmit: changeTitle
            })
        );
    };

    const dateDiff = useMemo(() => {
        try {
            if (!dueDate) {
                return;
            }
            const _dueDate = new Date(dueDate);
            if (isToday(_dueDate)) {
                return t("taskBoardDueDateToday");
            }
            const diffInDays = differenceInDays(_dueDate, startOfToday());
            if (diffInDays == 1) {
                return t("taskBoardDeadlineTomorrow")?.replace("${num}", `${diffInDays}`);
            } else if (diffInDays > 0) {
                return t("taskBoardRemainingDaysLabelDateInDays")?.replace("${num}", `${diffInDays}`);
            }
            return t("taskBoardDueDatePast")?.replace("${num}", `${Math.abs(diffInDays)}`);
        } catch (error) {
            console.error(error);
        }
    }, [dueDate]);

    const changeDisplayFormatToList = () => {
        setTaskBoardDefaultDisplayFormat(taskBoardId, "list");
        onViewTypeChange('list')
    };

    const changeDisplayFormatToWfsColumn = () => {
        setTaskBoardDefaultDisplayFormat(taskBoardId, "column");
        onViewTypeChange('column')
    };

    const onColorChange = (event: ChangeEvent<HTMLInputElement>) => {
        const color = event.target.value;
        setBoardColor(color.replace("#", ""));
    };

    const onColorPickerClick = () => {
        colorInputRef?.current?.focus?.();
        colorInputRef?.current?.click?.();
    }

    return (
        <div className={cn(styles.listTitle, boardState == "CLOSED" && styles.closedListTitle)}>
            <div className={styles.titleLabelContainer}>
                <h1 className={'line-clamp'}>
                    <b>{title}</b>
                </h1>

                <div className={'flex-1'}/>

                <div className={styles.actionBarRow}>
                    <div className={styles.viewTypeButtonContainer}>
                        <Button
                            onClick={changeDisplayFormatToList}
                            variant={displayFormat == "list" ? ButtonVariants.filled2 : ButtonVariants.filled}
                            className={styles.displayFormatButton}
                            data-tooltip-right={t("taskListTitleAndViewTypeListTooltip")}
                            heightVariant={ButtonHeight.short}
                        >
                            <IoList className={'icon'}/>
                            {t("taskListTitleAndViewTypeListLabel")}
                        </Button>
                        <Button
                            onClick={changeDisplayFormatToWfsColumn}
                            variant={displayFormat == "column" ? ButtonVariants.filled2 : ButtonVariants.filled}
                            className={styles.displayFormatButton}
                            data-tooltip-right={t("taskListTitleAndViewTypeStatusColumnsTooltip")}
                            heightVariant={ButtonHeight.short}
                        >
                            <IoStatsChart className={cn('icon', styles.wfsColumnIcon)}/>
                            {t("taskListTitleAndViewTypeStatusColumnsLabel")}
                        </Button>
                    </div>
                </div>
            </div>

            <div className={styles.titleActionBar}>

                <div className={styles.actionBarRow}>
                    <Button
                        disabled={boardState != "OPEN"}
                        variant={ButtonVariants.default}
                        heightVariant={ButtonHeight.short}
                        onClick={popDatePickerForDueDateUpdate}
                        data-tooltip-right={dueDate ? format(new Date(dueDate), t("dateFormat")) : undefined}
                        className={styles.actionButton}
                    >
                        <LuCalendarClock className={'icon'}/>
                        {dateDiff ? dateDiff : t("taskBoardAssignDueDate")}
                    </Button>
                    <Button
                        disabled={boardState != "OPEN"}
                        heightVariant={ButtonHeight.short}
                        onClick={popTitleChangeModal}
                        className={styles.actionButton}>
                        <IoPencil className="icon"/>
                        {t('taskBoardChangeTitleModalTitle')}
                    </Button>
                    <div>
                        <input
                            type={"color"}
                            value={boardColor ? `#${boardColor}` : getCssVariable("--c-primary-shade-1")}
                            onChange={onColorChange}
                            disabled={boardState == "CLOSED"}
                            className={styles.colorInput}
                            ref={colorInputRef}
                        />
                        <Button
                            variant={ButtonVariants.default}
                            heightVariant={ButtonHeight.short}
                            onClick={onColorPickerClick}
                            className={styles.actionButton}
                        >
                            <div
                                className={styles.boardColorIcon}
                                style={{backgroundColor: boardColor ? `#${boardColor}` : getCssVariable("--c-primary-shade-1")}}
                            />
                            {t('taskBoardColorLabel')}
                        </Button>
                    </div>
                    <Button
                        variant={ButtonVariants.default}
                        onClick={toggleBoardState}
                        disabled={!isTeamAdmin}
                        heightVariant={ButtonHeight.short}
                        className={styles.actionButton}
                    >
                        {boardState == "OPEN" ? <IoLockClosedOutline className={"icon"}/> :
                            <IoLockClosed className={"icon"}/>}
                        {t(`taskBoardLockButtonTooltip_${boardState}`)}
                    </Button>
                    <Button
                        disabled={boardState != "OPEN"}
                        variant={ButtonVariants.default}
                        heightVariant={ButtonHeight.short}
                        onClick={openSearchTaskModal}
                        className={styles.actionButton}
                    >
                        {/*{t("taskBoardAddTaskButtonLabel")}*/}
                        <LuSearch className={"icon"}/>
                        {t(`taskBoardSearchTaskButtonTooltip`)}
                    </Button>
                    <Button
                        disabled={boardState != "OPEN"}
                        variant={ButtonVariants.default}
                        heightVariant={ButtonHeight.short}
                        onClick={openNewTaskModal}
                        className={styles.actionButton}
                    >
                        <LuSquarePen className={"icon"}/>
                        {t(`taskBoardCreateTaskButtonTooltip`)}
                    </Button>
                </div>

                <div className={styles.actionBarRow}>
                    <TaskBoardQuickFilterBar team={team} taskBoardId={taskBoard.taskBoardId}/>
                </div>

            </div>
            <div className={'spacer-h-1'}/>
            <Line/>
        </div>
    );
};

export default TaskBoardTitle;
