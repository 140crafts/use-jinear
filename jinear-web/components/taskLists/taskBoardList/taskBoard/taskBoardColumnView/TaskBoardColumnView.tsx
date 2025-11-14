import React, { useState } from "react";
import styles from "./TaskBoardColumnView.module.css";
import TeamWorkflowStatusBoard from "@/components/teamWorkflowStatusBoard/TeamWorkflowStatusBoard";
import { useFilterFromTaskBoardQuery, useRetrieveFromTaskBoardQuery } from "@/api/taskBoardEntryApi";
import cn from "classnames";
import { queryStateJsonObjectParser, useQueryState } from "@/hooks/useQueryState";
import {
  ITaskBoardUrlStateMap
} from "@/components/taskLists/taskBoardList/taskBoard/taskBoardQuickFilterBar/TaskBoardQuickFilterBar";

interface TaskBoardColumnViewProps {
  taskBoardId: string;
  teamId: string;
  className?: string;
}

const TaskBoardColumnView: React.FC<TaskBoardColumnViewProps> = ({
                                                                   taskBoardId,
                                                                   teamId,
                                                                   className
                                                                 }) => {
  const [page, setPage] = useState<number>(0);
  const taskBoardFilterMap = useQueryState<ITaskBoardUrlStateMap>("board-filter-map", queryStateJsonObjectParser) ?? {};
  const thisBoardsFilter = taskBoardFilterMap[taskBoardId] ?? {};

  const { data: taskBoardElementsResponse, isFetching, isLoading } = useFilterFromTaskBoardQuery({
    taskBoardId,
    page,
    body: thisBoardsFilter
  });

  return (
    <div className={cn(styles.statusBoardContainer, className)}>
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