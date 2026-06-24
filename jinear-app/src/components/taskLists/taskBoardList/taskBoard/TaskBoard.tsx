import React from "react";
import styles from "./TaskBoard.module.css";
import TaskBoardTitle from "@/components/taskLists/taskBoardList/taskBoard/taskBoardTitle/TaskBoardTitle";
import type {TaskBoardDto, TeamDto, WorkspaceDto} from "@/be/jinear-core";
import TaskBoardElementList
    from "@/components/taskLists/taskBoardList/taskBoard/taskBoardElementList/TaskBoardElementList";
import TaskBoardColumnView
    from "@/components/taskLists/taskBoardList/taskBoard/taskBoardColumnView/TaskBoardColumnView";
import {
    queryStateJsonObjectParser,
    queryStateObjectToJsonStringConverter,
    useQueryState,
    useSetQueryState
} from "@/hooks/useQueryState";
import type {
    ITaskBoardUrlStateMap
} from "@/components/taskLists/taskBoardList/taskBoard/taskBoardQuickFilterBar/TaskBoardQuickFilterBar";

interface TaskBoardProps {
    taskBoard: TaskBoardDto;
    team: TeamDto;
    workspace: WorkspaceDto;
    staticViewType?: "list" | "column";
}

const getTaskBoardDefaultDisplayFormat = (taskBoardId: string): "list" | "column" => {
    const defaultViewFormat = "list";
    if (typeof window === "object") {
        const viewFormat = localStorage.getItem(`task-board-view-${taskBoardId}`) || defaultViewFormat;
        return ["list", "column"].indexOf(viewFormat) != -1 ? viewFormat as ("list" | "column") : defaultViewFormat;
    }
    return defaultViewFormat;
};

export const setTaskBoardDefaultDisplayFormat = (taskBoardId: string, format: "list" | "column") => {
    if (typeof window === "object") {
        localStorage.setItem(`task-board-view-${taskBoardId}`, format);
    }
};

const TaskBoard: React.FC<TaskBoardProps> = ({
                                                 taskBoard,
                                                 workspace,
                                                 team
                                             }) => {
    const taskBoardFilterMap = useQueryState<ITaskBoardUrlStateMap>("board-filter-map", queryStateJsonObjectParser) ?? {};

    const displayFormat = taskBoardFilterMap?.[taskBoard.taskBoardId]?.viewType || getTaskBoardDefaultDisplayFormat(taskBoard.taskBoardId);
    const currentPage = taskBoardFilterMap?.[taskBoard.taskBoardId]?.page ?? 0;
    const setQueryState = useSetQueryState();

    const setPage = (nextPage?: number) => {
        const page = nextPage ?? 0;
        const finalTaskBoardFilterMap = taskBoardFilterMap ?? {};
        finalTaskBoardFilterMap[taskBoard.taskBoardId] = finalTaskBoardFilterMap[taskBoard.taskBoardId] ?? {};
        // @ts-expect-error ignore
        finalTaskBoardFilterMap[taskBoard.taskBoardId].page = page;
        setQueryState("board-filter-map", queryStateObjectToJsonStringConverter(finalTaskBoardFilterMap));
    };

    const setViewtype = (nextViewType: 'column' | 'list') => {
        const finalTaskBoardFilterMap = taskBoardFilterMap ?? {};
        finalTaskBoardFilterMap[taskBoard.taskBoardId] = finalTaskBoardFilterMap[taskBoard.taskBoardId] ?? {};
        // @ts-expect-error ignore
        finalTaskBoardFilterMap[taskBoard.taskBoardId].viewType = nextViewType;
        setQueryState("board-filter-map", queryStateObjectToJsonStringConverter(finalTaskBoardFilterMap));
    }

    return (
        <div className={styles.container}>
            <TaskBoardTitle
                taskBoard={taskBoard}
                team={team}
                workspace={workspace}
                displayFormat={displayFormat}
                onViewTypeChange={setViewtype}
            />
            <TaskBoardElementList
                taskBoardId={taskBoard.taskBoardId}
                boardState={taskBoard.state}
                className={displayFormat == "list" ? styles.visible : styles.hidden}
                page={currentPage}
                setPage={setPage}
            />
            <TaskBoardColumnView
                taskBoardId={taskBoard.taskBoardId}
                teamId={team.teamId}
                className={displayFormat == "column" ? styles.visible : styles.hidden}
                page={currentPage}
                setPage={setPage}
            />
        </div>
    );
};

export default TaskBoard;