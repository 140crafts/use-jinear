import { useRetrieveWithAssigneeQuery } from "@/store/api/taskListingApi";
import React, { useState } from "react";
import BaseTaskList from "../baseTaskList/BaseTaskList";

interface PaginatedFromTeamWithAssigneeTaskListProps {
  listTitle?: string;
  assigneeId: string;
  workspaceId: string;
  teamId: string;
}

const PaginatedFromTeamWithAssigneeTaskList: React.FC<PaginatedFromTeamWithAssigneeTaskListProps> = ({
  assigneeId,
  workspaceId,
  teamId,
  listTitle = "",
}) => {
  const [page, setPage] = useState<number>(0);

  const {
    data: response,
    isSuccess,
    isError,
    isFetching,
    isLoading,
  } = useRetrieveWithAssigneeQuery({
    workspaceId,
    teamId,
    assigneeId,
    page,
  });

  return (
    <BaseTaskList
      id={`assigned-to-assignee-from-team-tasks-${workspaceId}-${teamId}-${assigneeId}`}
      name={listTitle}
      response={response}
      isFetching={isFetching}
      isLoading={isLoading}
      page={page}
      setPage={setPage}
    />
  );
};

export default PaginatedFromTeamWithAssigneeTaskList;
