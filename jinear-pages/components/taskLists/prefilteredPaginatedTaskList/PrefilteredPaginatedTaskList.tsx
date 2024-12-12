import { TaskFilterRequest } from "@/model/be/jinear-core";
import { useFilterTasksQuery } from "@/store/api/taskListingApi";
import React, { useState } from "react";
import BaseTaskList from "../baseTaskList/BaseTaskList";

interface PrefilteredPaginatedTaskListProps {
  id: string;
  filter: TaskFilterRequest;
  containerClassName?: string;
  contentContainerClassName?: string;
}

const PrefilteredPaginatedTaskList: React.FC<PrefilteredPaginatedTaskListProps> = ({
  id,
  filter,
  containerClassName,
  contentContainerClassName,
}) => {
  const [page, setPage] = useState<number>(0);
  const { data: filterResponse, isFetching, isLoading } = useFilterTasksQuery({ ...filter, page }, { skip: filter == null });

  return (
    <BaseTaskList
      id={`pre-filtered-pagineted-task-list-${id}`}
      name={""}
      response={filterResponse}
      isFetching={isFetching}
      isLoading={isLoading}
      page={page}
      setPage={setPage}
      containerClassName={containerClassName}
      contentContainerClassName={contentContainerClassName}
    />
  );
};

export default PrefilteredPaginatedTaskList;
