import type {TaskFilterRequest, TeamDto, WorkspaceDto} from "@/model/be/jinear-core";

import Logger from "@/util/logger";
import React from "react";
import styles from "./TaskListFilterBar.module.scss";
import AssigneeFilterButton from "./assigneeFilterButton/AssigneeFilterButton";
import TaskListFilterBarContext from "./context/TaskListFilterBarContext";
import FromDatePickerButton from "./fromDatePickerButton/FromDatePickerButton";
import OwnerFilterButton from "./ownerFilterButton/OwnerFilterButton";
import QuickFilterBar from "./quickFilterBar/QuickFilterBar";
import ToDatePickerButton from "./toDatePickerButton/ToDatePickerButton";
import TopicFilterButton from "./topicFilterButton/TopicFilterButton";
import WorkflowStatusFilterButton from "./workflowStatusFilterButton/WorkflowStatusFilterButton";
import TaskListTitleAndViewType, {
    type TaskDisplayFormat
} from "@/components/taskLists/taskListTitleAndViewType/TaskListTitleAndViewType.tsx";
import {useFilterTasksQuery} from "@/api/taskListingApi.ts";
import {queryStateAnyToStringConverter, useQueryState, useSetQueryState} from "@/hooks/useQueryState.ts";

interface TaskListFilterBarProps {
    workspace: WorkspaceDto;
    team: TeamDto;
    onFilterChange?: (filter: TaskFilterRequest) => void;
}

const logger = Logger("TaskListFilterBar");

const TaskListFilterBar: React.FC<TaskListFilterBarProps> = ({workspace, team}) => {
    const setQueryState = useSetQueryState();
    const displayFormat = useQueryState<TaskDisplayFormat>("displayFormat") || "LIST";

    const setDisplayFormat = (displayFormat: TaskDisplayFormat) => {
        setQueryState("displayFormat", queryStateAnyToStringConverter(displayFormat));
    };

    const onTaskDisplayFormatChange = (format: TaskDisplayFormat) => {
        if (format != displayFormat) {
            setDisplayFormat(format);
        }
    };
    return (
        <TaskListFilterBarContext.Provider value={{team, workspace}}>
            <div className={styles.container}>
                <div className={styles.containerRow}>
                    <div className={styles.filterContainer}>
                        <FromDatePickerButton/>
                        <ToDatePickerButton/>
                        <WorkflowStatusFilterButton/>
                        <AssigneeFilterButton/>
                        <OwnerFilterButton/>
                        <TopicFilterButton/>
                    </div>
                    <TaskListTitleAndViewType
                        teamUsername={team.username}
                        taskDisplayFormat={displayFormat}
                        onTaskDisplayFormatChange={onTaskDisplayFormatChange}
                    />
                </div>

                <QuickFilterBar className={styles.quickFilters}/>

            </div>
        </TaskListFilterBarContext.Provider>
    );
};

export default React.memo(TaskListFilterBar, (prevProps, nextProps) => {
    const prev = JSON.stringify(prevProps);
    const next = JSON.stringify(nextProps);
    const areEqual = prev == next;
    logger.log({prevProps, nextProps, areEqual});
    return areEqual;
});
