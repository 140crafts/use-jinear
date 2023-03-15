import { useRetrieveAllTasksQuery } from "@/store/api/taskListingApi";
import useTranslation from "locales/useTranslation";
import React, { useState } from "react";
import BaseTaskList from "../baseTaskList/BaseTaskList";

interface PaginatedAllTasksListProps {
  workspaceId: string;
  teamId: string;
  name: string;
}

const PaginatedAllTasksList: React.FC<PaginatedAllTasksListProps> = ({ workspaceId, teamId, name }) => {
  const { t } = useTranslation();
  const [page, setPage] = useState<number>(0);

  const {
    data: response,
    isLoading,
    isFetching,
  } = useRetrieveAllTasksQuery({
    workspaceId,
    teamId,
    page,
  });

  return (
    <BaseTaskList
      id={`all-tasks-${workspaceId}-${teamId}`}
      name={name}
      response={response}
      isFetching={isFetching}
      isLoading={isLoading}
      page={page}
      setPage={setPage}
    />
  );
};

export default PaginatedAllTasksList;
