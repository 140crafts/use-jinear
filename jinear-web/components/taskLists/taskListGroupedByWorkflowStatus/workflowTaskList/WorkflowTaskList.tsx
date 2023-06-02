import { useRetrieveFromWorkflowStatusQuery } from "@/store/api/taskListingApi";
import React, { useState } from "react";
import BaseTaskList from "../../baseTaskList/BaseTaskList";

interface WorkflowTaskListProps {
  workspaceId: string;
  teamId: string;
  workflowStatusId: string;
  name: string;
}

const WorkflowTaskList: React.FC<WorkflowTaskListProps> = ({ workspaceId, teamId, workflowStatusId, name }) => {
  const [page, setPage] = useState<number>(0);
  const {
    data: workflowTaskListResponse,
    isLoading: isWorkflowTaskListLoading,
    isFetching: isWorkflowTaskListFetching,
  } = useRetrieveFromWorkflowStatusQuery(
    {
      workspaceId,
      teamId,
      workflowStatusId,
      page,
    },
    { skip: workflowStatusId == null }
  );

  const isLoading = isWorkflowTaskListLoading;
  const isFetching = isWorkflowTaskListFetching;
  const response = workflowTaskListResponse;

  return (
    <BaseTaskList
      id={`task-list-workflow-status-${workspaceId}-${teamId}-${workflowStatusId}`}
      name={name}
      response={response}
      isFetching={isFetching}
      isLoading={isLoading}
      page={page}
      setPage={setPage}
    />
  );
};

export default WorkflowTaskList;
