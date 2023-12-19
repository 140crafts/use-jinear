import Line from "@/components/line/Line";
import { TaskFilterRequest, TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import { useFilterTasksQuery } from "@/store/api/taskListingApi";
import Logger from "@/utils/logger";
import { createUrl } from "@/utils/urlUtils";
import { ReadonlyURLSearchParams, useRouter, useSearchParams } from "next/navigation";
import React, { useEffect, useState } from "react";
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
  topicIds?: string[];
  ownerIds?: string[];
  assigneeIds?: string[];
  workflowStatusIdList?: string[];
  timespanStart?: Date;
  timespanEnd?: Date;
  hasPreciseFromDate?: boolean;
  hasPreciseToDate?: boolean;
  workflowStatusBoardClassName?: string;
  pathname: string;
}

const logger = Logger("MultiViewTaskList");

const convertToSearchParams = (searchParams: ReadonlyURLSearchParams, filter: TaskFilterRequest): URLSearchParams => {
  logger.log({ convertToSearchParams: filter, searchParams: searchParams.toString() });
  const urlSearchParams = new URLSearchParams(searchParams);
  filter.page && urlSearchParams.set("page", filter.page.toString());
  filter.workspaceId && urlSearchParams.set("workspaceId", filter.workspaceId);
  filter.teamIdList && filter.teamIdList.length != 0 && urlSearchParams.set("teamIdList", filter.teamIdList.toString());
  filter.topicIds && filter.topicIds.length != 0 && urlSearchParams.set("topicIds", filter.topicIds.toString());
  filter.ownerIds && filter.ownerIds.length != 0 && urlSearchParams.set("ownerIds", filter.ownerIds.toString());
  filter.assigneeIds && filter.assigneeIds.length != 0 && urlSearchParams.set("assigneeIds", filter.assigneeIds.toString());
  filter.workflowStatusIdList &&
    filter.workflowStatusIdList.length != 0 &&
    urlSearchParams.set("workflowStatusIdList", filter.workflowStatusIdList.toString());
  filter.timespanStart && urlSearchParams.set("timespanStart", new Date(filter.timespanStart).toISOString());
  filter.timespanEnd && urlSearchParams.set("timespanEnd", new Date(filter.timespanEnd).toISOString());
  return urlSearchParams;
};

const convertToFilter = (searchParams: ReadonlyURLSearchParams): TaskFilterRequest => {
  logger.log({ convertToFilter: searchParams.toString() });
  const filter = {} as TaskFilterRequest;
  const page = searchParams.get("page");
  filter.page = page ? parseInt(page) : undefined;
  const workspaceId = searchParams.get("workspaceId");
  if (workspaceId) {
    filter.workspaceId = workspaceId;
  }
  const teamIdList = searchParams.get("teamIdList");
  if (teamIdList) {
    filter.teamIdList = teamIdList.split(",");
  }
  const topicIds = searchParams.get("topicIds");
  if (topicIds) {
    filter.topicIds = topicIds.split(",");
  }
  const ownerIds = searchParams.get("ownerIds");
  if (ownerIds) {
    filter.ownerIds = ownerIds.split(",");
  }
  const assigneeIds = searchParams.get("assigneeIds");
  if (assigneeIds) {
    filter.assigneeIds = assigneeIds.split(",");
  }
  const workflowStatusIdList = searchParams.get("workflowStatusIdList");
  if (workflowStatusIdList) {
    filter.workflowStatusIdList = workflowStatusIdList.split(",");
  }
  const timespanStart = searchParams.get("timespanStart");
  if (timespanStart) {
    filter.timespanStart = new Date(timespanStart);
  }
  const timespanEnd = searchParams.get("timespanEnd");
  if (timespanEnd) {
    filter.timespanEnd = new Date(timespanEnd);
  }
  return filter;
};

const MultiViewTaskList: React.FC<MultiViewTaskListProps> = ({
  workspace,
  team,
  title,
  activeDisplayFormat = "LIST",
  topicIds,
  ownerIds,
  assigneeIds,
  workflowStatusIdList,
  timespanStart,
  timespanEnd,
  hasPreciseFromDate,
  hasPreciseToDate,
  workflowStatusBoardClassName,
  pathname,
}) => {
  const router = useRouter();
  const searchParams = useSearchParams();
  const [displayFormat, setDisplayFormat] = useState<TaskDisplayFormat>(activeDisplayFormat);
  const [page, setPage] = useState<number>(0);
  const [filter, setFilter] = useState<TaskFilterRequest>(convertToFilter(searchParams));

  useEffect(() => {
    setFilter(convertToFilter(searchParams));
  }, [searchParams]);

  const onTaskListFilterChange = (taskFilterFromBar: TaskFilterRequest) => {
    const nextFilter = { ...taskFilterFromBar, page };
    // setFilter(nextFilter);
    const urlSearchParams = convertToSearchParams(searchParams, nextFilter);
    router.push(createUrl(pathname, urlSearchParams));
    logger.log({ pathname, urlSearchParams: urlSearchParams.toString() });
  };

  const onTaskDisplayFormatChange = (format: TaskDisplayFormat) => {
    if (format != displayFormat) {
      setDisplayFormat(format);
    }
  };

  //@ts-ignore
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
      <TaskListFilterBar
        workspace={workspace}
        team={team}
        topicIds={topicIds}
        ownerIds={ownerIds}
        assigneeIds={assigneeIds}
        workflowStatusIdList={workflowStatusIdList}
        timespanStart={timespanStart}
        timespanEnd={timespanEnd}
        hasPreciseFromDate={hasPreciseFromDate}
        hasPreciseToDate={hasPreciseToDate}
        onFilterChange={onTaskListFilterChange}
      />
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
