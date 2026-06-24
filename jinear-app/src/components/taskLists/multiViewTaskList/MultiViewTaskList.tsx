import Line from "@/components/line/Line";
import {
    queryStateAnyToStringConverter,
    queryStateArrayParser,
    queryStateBooleanParser,
    queryStateIntParser,
    queryStateIsoDateParser,
    useQueryState,
    useSetQueryState,
    useSetQueryStateMultiple
} from "@/hooks/useQueryState";
import type {TaskFilterRequest, TeamDto, TeamWorkflowStateGroup, WorkspaceDto} from "@/model/be/jinear-core";
import {useFilterTasksQuery} from "@/store/api/taskListingApi";
import Logger from "@/util/logger";
import React, {useEffect} from "react";
import BaseTaskList from "../baseTaskList/BaseTaskList";
import TaskListFilterBar from "../taskListFilterBar/TaskListFilterBar";
import TaskListTitleAndViewType, {type TaskDisplayFormat} from "../taskListTitleAndViewType/TaskListTitleAndViewType";
import TaskWorkflowStatusBoardView from "../taskWorkflowStatusBoardView/TaskWorkflowStatusBoardView";
import styles from "./MultiViewTaskList.module.css";
import useTranslation from "@/locals/useTranslation.ts";
import {useParams} from "react-router-dom";
import {useTypedSelector} from "@/store";
import {selectWorkspaceFromWorkspaceUsername} from "@/slice/accountSlice.ts";

interface MultiViewTaskListProps {
    workspace: WorkspaceDto;
    team: TeamDto;
    activeDisplayFormat: TaskDisplayFormat;
    workflowStatusBoardClassName?: string;
}

export interface ExtendedTaskFilterRequest extends TaskFilterRequest {
    hasPreciseFromDate?: boolean | null;
    hasPreciseToDate?: boolean | null;
}

export const getTeamDefaultDisplayFormat = (teamUsername: string): TaskDisplayFormat => {
    const defaultViewFormat = "WFS_COLUMN";
    if (typeof window === "object") {
        const viewFormat = localStorage.getItem(`df-${teamUsername}`) || defaultViewFormat;
        return ["LIST", "WFS_COLUMN"].indexOf(viewFormat) != -1 ? viewFormat as TaskDisplayFormat : defaultViewFormat;
    }
    return defaultViewFormat;
};

const logger = Logger("MultiViewTaskList");

const MultiViewTaskList: React.FC<MultiViewTaskListProps> = ({
                                                                 workspace,
                                                                 team,
                                                                 activeDisplayFormat = "WFS_COLUMN",
                                                                 workflowStatusBoardClassName
                                                             }) => {
    const setQueryState = useSetQueryState();
    const setQueryStateMultiple = useSetQueryStateMultiple();
    const page = useQueryState<number>("page", queryStateIntParser) || 0;
    const workspaceId = useQueryState<string>("workspaceId") || workspace.workspaceId;
    const teamIdList = useQueryState<string[]>("teamIdList", queryStateArrayParser) || [team.teamId];
    const topicIds = useQueryState<string[]>("topicIds", queryStateArrayParser);
    const ownerIds = useQueryState<string[]>("ownerIds", queryStateArrayParser);
    const assigneeIds = useQueryState<string[]>("assigneeIds", queryStateArrayParser);
    const workflowStatusIdList = useQueryState<string[]>("workflowStatusIdList", queryStateArrayParser);
    const workflowStateGroups = useQueryState<TeamWorkflowStateGroup[]>("workflowStateGroups", queryStateArrayParser);
    const timespanStart = useQueryState<Date>("timespanStart", queryStateIsoDateParser);
    const timespanEnd = useQueryState<Date>("timespanEnd", queryStateIsoDateParser);
    const hasPreciseFromDate = useQueryState<boolean>("hasPreciseFromDate", queryStateBooleanParser);
    const hasPreciseToDate = useQueryState<boolean>("hasPreciseToDate", queryStateBooleanParser);
    const displayFormat = useQueryState<TaskDisplayFormat>("displayFormat") || "LIST";

    const filter = {
        page,
        workspaceId,
        teamIdList,
        topicIds,
        ownerIds,
        assigneeIds,
        workflowStatusIdList,
        workflowStateGroups,
        timespanStart,
        timespanEnd,
        hasPreciseFromDate,
        hasPreciseToDate
    };

    useEffect(() => {
        if (team && workspace) {
            setQueryStateMultiple(
                new Map([
                    ["workspaceId", queryStateAnyToStringConverter(workspace.workspaceId)],
                    ["teamIdList", queryStateAnyToStringConverter([team.teamId])],
                    ["displayFormat", activeDisplayFormat]
                ])
            );
        }
    }, [team, workspace, activeDisplayFormat]);

    const setPage = (nextPage?: number) => {
        setQueryState("page", queryStateAnyToStringConverter(nextPage));
    };

    const {
        currentData: currentFilterResponse,
        data: retainedFilterResponse,
        isFetching,
        isLoading
    } = useFilterTasksQuery(filter, {
        skip: filter == null || filter.workspaceId == null || filter.teamIdList == null || filter.teamIdList?.length == 0
    });

    const filterResponse = currentFilterResponse ?? retainedFilterResponse;

    return (
        <div className={styles.container}>
            <TaskListFilterBar workspace={workspace} team={team}/>
            <Line/>
            {displayFormat == "LIST" && (
                <BaseTaskList
                    id={`filtered-tasks-${workspace?.workspaceId}-${team?.teamId}`}
                    name={""}
                    response={filterResponse}
                    isFetching={isFetching}
                    isLoading={isLoading}
                    page={page}
                    setPage={setPage}
                    paginationPosition="TOP"
                />
            )}
            {displayFormat == "WFS_COLUMN" && (
                <TaskWorkflowStatusBoardView
                    id={`filtered-tasks-wfs-column-view-${workspace?.workspaceId}-${team?.teamId}`}
                    teamId={team.teamId}
                    taskList={filterResponse?.data?.content || []}
                    isTaskListingLoading={isFetching}
                    workflowStatusBoardClassName={workflowStatusBoardClassName}
                    isFetching={isFetching}
                    isLoading={isLoading}
                    page={page}
                    setPage={setPage}
                    response={filterResponse}
                    paginationPosition="TOP"
                />
            )}
        </div>
    );
};

export default MultiViewTaskList;
