import Line from "@/components/line/Line";
import { TaskFilterRequest, TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import { useFilterTasksQuery } from "@/store/api/taskListingApi";
import React, { useState } from "react";
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
}

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
}) => {
  const [displayFormat, setDisplayFormat] = useState<TaskDisplayFormat>(activeDisplayFormat);
  const [page, setPage] = useState<number>(0);
  const [filter, setFilter] = useState<TaskFilterRequest>();

  const onTaskListFilterChange = (taskFilterFromBar: TaskFilterRequest) => {
    setFilter({ ...taskFilterFromBar, page });
  };

  const onTaskDisplayFormatChange = (format: TaskDisplayFormat) => {
    if (format != displayFormat) {
      setDisplayFormat(format);
    }
  };

  //@ts-ignore
  const { data: filterResponse, isFetching, isLoading } = useFilterTasksQuery(filter, { skip: filter == null });

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
