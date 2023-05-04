import { useRetrieveAssignedToCurrentAccountQuery } from "@/store/api/taskListingApi";
import useTranslation from "locales/useTranslation";
import React, { useState } from "react";
import BaseTaskList from "../baseTaskList/BaseTaskList";

interface PaginatedAssignedToCurrentAccountTaskListProps {
  workspaceId: string;
}

const PaginatedAssignedToCurrentAccountTaskList: React.FC<PaginatedAssignedToCurrentAccountTaskListProps> = ({ workspaceId }) => {
  const { t } = useTranslation();
  const [page, setPage] = useState<number>(0);

  const {
    data: response,
    isSuccess,
    isError,
    isFetching,
    isLoading,
  } = useRetrieveAssignedToCurrentAccountQuery({
    workspaceId,
    page,
  });

  return (
    <BaseTaskList
      id={`assigned-to-me-tasks-${workspaceId}`}
      name={t("assignedToMeTaskListName")}
      response={response}
      isFetching={isFetching}
      isLoading={isLoading}
      page={page}
      setPage={setPage}
    />
  );
};

export default PaginatedAssignedToCurrentAccountTaskList;
