import TeamWorkflowStatusBoard from "@/components/teamWorkflowStatusBoard/TeamWorkflowStatusBoard";
import type { TaskDto, TaskListingPaginatedResponse } from "@/model/be/jinear-core";
import React from "react";
import styles from "./TaskWorkflowStatusBoardView.module.scss";
import Pagination from "@/components/pagination/Pagination";
import type { PaginationPosition } from "@/components/taskLists/baseTaskList/BaseTaskList";

interface TaskWorkflowStatusBoardViewProps {
  teamId: string;
  taskList: TaskDto[];
  isTaskListingLoading: boolean;
  workflowStatusBoardClassName?: string;
  isFetching: boolean;
  isLoading: boolean;
  page: number;
  setPage: React.Dispatch<React.SetStateAction<number>> | ((nextPage?: number) => void);
  paginationPosition?: PaginationPosition;
  hidePaginationOnSinglePages?: boolean;
  response?: TaskListingPaginatedResponse;
  id: string;
}

const TaskWorkflowStatusBoardView: React.FC<TaskWorkflowStatusBoardViewProps> = ({
                                                                                   id,
                                                                                   teamId,
                                                                                   taskList,
                                                                                   isTaskListingLoading,
                                                                                   workflowStatusBoardClassName,
                                                                                   isFetching,
                                                                                   isLoading,
                                                                                   page,
                                                                                   setPage,
                                                                                   paginationPosition = "TOP",
                                                                                   hidePaginationOnSinglePages = true,
                                                                                   response
                                                                                 }) => {
  const emptyOrSinglePage = response?.data?.totalPages == 0 || response?.data?.totalPages == 1;

  return (
    <div id={id} className={styles.container}>
      <div className={styles.actionBarContainer}>
        {!(hidePaginationOnSinglePages && emptyOrSinglePage) && response && paginationPosition == "TOP" && (
          <Pagination
            id={`${id}-paginator`}
            className={styles.pagination}
            pageNumber={response.data.number}
            pageSize={response.data.size}
            totalPages={response.data.totalPages}
            totalElements={response.data.totalElements}
            hasPrevious={response.data.hasPrevious}
            hasNext={response.data.hasNext}
            isLoading={isLoading || isFetching}
            page={page}
            setPage={setPage}
          />
        )}
      </div>
      <span className={styles.statusBoardContainer}>
        {teamId && (
          <TeamWorkflowStatusBoard
            teamId={teamId}
            taskList={taskList}
            isTaskListingLoading={isTaskListingLoading}
            className={workflowStatusBoardClassName}
          />
        )}
      </span>
      <div className={styles.footer}>
        {!(hidePaginationOnSinglePages && emptyOrSinglePage) && response && paginationPosition == "BOTTOM" && (
          <Pagination
            id={`${id}-paginator`}
            className={styles.pagination}
            pageNumber={response.data.number}
            pageSize={response.data.size}
            totalPages={response.data.totalPages}
            totalElements={response.data.totalElements}
            hasPrevious={response.data.hasPrevious}
            hasNext={response.data.hasNext}
            isLoading={isLoading || isFetching}
            page={page}
            setPage={setPage}
          />
        )}
      </div>
    </div>
  );
};

export default TaskWorkflowStatusBoardView;
