import { useFilterWorkspaceActivitiesQuery } from "@/store/api/workspaceActivityApi";
import React, { useState } from "react";
import PaginatedActivityList from "../paginatedActivityList/PaginatedActivityList";
import styles from "./LastTaskActivitiesList.module.css";

interface LastTaskActivitiesListProps {
  workspaceId: string;
  taskId: string;
  listTitle?: string;
  listTitleClassName?: string;
}

const LastTaskActivitiesList: React.FC<LastTaskActivitiesListProps> = ({
  workspaceId,
  taskId,
  listTitle = "",
  listTitleClassName,
}) => {
  const [page, setPage] = useState<number>(0);
  const { data: response, isLoading, isFetching } = useFilterWorkspaceActivitiesQuery({ workspaceId, taskIds: [taskId], page });
  // const { data: response, isLoading, isFetching } = useRetrieveActivitiesFromTaskQuery({ workspaceId, taskId, page });

  return (
    <div className={styles.container}>
      <PaginatedActivityList
        id={"last-workspace-task-activities-list"}
        name={listTitle}
        listNameClassName={listTitleClassName}
        response={response}
        isFetching={isFetching}
        isLoading={isLoading}
        page={page}
        setPage={setPage}
        ignoreGrouping={true}
        hidePaginationOnSinglePages={true}
      />
    </div>
  );
};

export default LastTaskActivitiesList;
