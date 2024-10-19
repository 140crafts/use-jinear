import Line from "@/components/line/Line";
import {
  queryStateAnyToStringConverter,
  queryStateArrayParser,
  queryStateBooleanParser,
  queryStateIntParser,
  queryStateIsoDateParser,
  useQueryState,
  useSetQueryState,
  useSetQueryStateMultiple,
} from "@/hooks/useQueryState";
import { TaskFilterRequest, TeamDto, TeamWorkflowStateGroup, WorkspaceDto } from "@/model/be/jinear-core";
import { useFilterTasksQuery } from "@/store/api/taskListingApi";
import Logger from "@/utils/logger";
import React, { useEffect } from "react";
import BaseTaskList from "../baseTaskList/BaseTaskList";
import TaskListFilterBar from "../taskListFilterBar/TaskListFilterBar";
import TaskListTitleAndViewType, { TaskDisplayFormat } from "../taskListTitleAndViewType/TaskListTitleAndViewType";
import TaskWorkflowStatusBoardView from "../taskWorkflowStatusBoardView/TaskWorkflowStatusBoardView";
import styles from "./MultiViewTaskList.module.css";

interface MultiViewTaskListProps {
  workspace: WorkspaceDto;
  team: TeamDto;
  title: string;
  activeDisplayFormat: TaskDisplayFormat;
  workflowStatusBoardClassName?: string;
  pathname: string;
}

export interface ExtendedTaskFilterRequest extends TaskFilterRequest {
  hasPreciseFromDate?: boolean;
  hasPreciseToDate?: boolean;
}

const logger = Logger("MultiViewTaskList");

const MultiViewTaskList: React.FC<MultiViewTaskListProps> = ({
  workspace,
  team,
  title,
  activeDisplayFormat = "WFS_COLUMN",
  workflowStatusBoardClassName,
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
    hasPreciseToDate,
  };

  useEffect(() => {
    if (team && workspace) {
      setQueryStateMultiple(
        new Map([
          ["workspaceId", queryStateAnyToStringConverter(workspace.workspaceId)],
          ["teamIdList", queryStateAnyToStringConverter([team.teamId])],
          ["displayFormat", activeDisplayFormat],
        ])
      );
    }
  }, [team, workspace, activeDisplayFormat]);

  const setPage = (nextPage?: number) => {
    setQueryState("page", queryStateAnyToStringConverter(nextPage));
  };

  const setDisplayFormat = (displayFormat: TaskDisplayFormat) => {
    setQueryState("displayFormat", queryStateAnyToStringConverter(displayFormat));
  };

  const onTaskDisplayFormatChange = (format: TaskDisplayFormat) => {
    if (format != displayFormat) {
      setDisplayFormat(format);
    }
  };

  const {
    data: filterResponse,
    isFetching,
    isLoading,
  } = useFilterTasksQuery(filter, {
    skip: filter == null || filter.workspaceId == null || filter.teamIdList == null || filter.teamIdList?.length == 0,
  });

  return (
    <div className={styles.container}>
      <TaskListTitleAndViewType
        title={title}
        taskDisplayFormat={displayFormat}
        onTaskDisplayFormatChange={onTaskDisplayFormatChange}
      />
      <TaskListFilterBar workspace={workspace} team={team} />
      <Line />
      {displayFormat == "LIST" && (
        <BaseTaskList
          id={`filtered-tasks-${workspace?.workspaceId}-${team?.teamId}`}
          name={""}
          response={filterResponse}
          isFetching={isFetching}
          isLoading={isLoading}
          page={page}
          setPage={setPage}
          paginationPosition="BOTTOM"
        />
      )}
      {displayFormat == "WFS_COLUMN" && (
        <TaskWorkflowStatusBoardView
          teamId={team.teamId}
          taskList={filterResponse?.data?.content || []}
          isTaskListingLoading={isFetching}
          workflowStatusBoardClassName={workflowStatusBoardClassName}
        />
      )}
    </div>
  );
};

export default MultiViewTaskList;
