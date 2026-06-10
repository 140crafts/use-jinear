import React, { useState } from "react";
import styles from "./TaskBoardColumnView.module.scss";
import TeamWorkflowStatusBoard from "@/components/teamWorkflowStatusBoard/TeamWorkflowStatusBoard";
import { useFilterFromTaskBoardQuery, useRetrieveFromTaskBoardQuery } from "@/api/taskBoardEntryApi";
import cn from "classnames";
import { queryStateJsonObjectParser, useQueryState } from "@/hooks/useQueryState";
import {
  ITaskBoardUrlStateMap
} from "@/components/taskLists/taskBoardList/taskBoard/taskBoardQuickFilterBar/TaskBoardQuickFilterBar";
import Pagination from "@/components/pagination/Pagination";

interface TaskBoardColumnViewProps {
  taskBoardId: string;
  teamId: string;
  className?: string;
  page: number;
  setPage: (nextPage?: number) => void;
}

const TaskBoardColumnView: React.FC<TaskBoardColumnViewProps> = ({
                                                                   taskBoardId,
                                                                   teamId,
                                                                   className,
                                                                   page = 0,
                                                                   setPage
                                                                 }) => {
  const taskBoardFilterMap = useQueryState<ITaskBoardUrlStateMap>("board-filter-map", queryStateJsonObjectParser) ?? {};
  const thisBoardsFilter = taskBoardFilterMap[taskBoardId] ?? {};

  const { data: taskBoardElementsResponse, isFetching, isLoading } = useFilterFromTaskBoardQuery({
    taskBoardId,
    page,
    body: thisBoardsFilter
  });

  return (
    <div className={cn(styles.statusBoardContainer, className)}>
      <Pagination
        id={`${taskBoardId}-paginator`}
        className={styles.pagination}
        pageNumber={taskBoardElementsResponse?.data.number ?? 0}
        pageSize={taskBoardElementsResponse?.data.size ?? 0}
        totalPages={taskBoardElementsResponse?.data.totalPages ?? 0}
        totalElements={taskBoardElementsResponse?.data.totalElements ?? 0}
        hasPrevious={taskBoardElementsResponse?.data.hasPrevious ?? false}
        hasNext={taskBoardElementsResponse?.data.hasNext ?? false}
        isLoading={isLoading || isFetching}
        page={page}
        setPage={setPage}
      />
      <div className={"spacer-h-1"} />

      <TeamWorkflowStatusBoard
        teamId={teamId}
        taskList={taskBoardElementsResponse?.data?.content?.map(a => a.task) || []}
        isTaskListingLoading={isFetching}
        className={styles.workflowStatusBoard}
      />
    </div>
  );
};

export default TaskBoardColumnView;