import { useFilterWorkspaceActivitiesQuery } from "@/store/api/workspaceActivityApi";
import React, { useState } from "react";
import PaginatedActivityList from "../paginatedActivityList/PaginatedActivityList";
import styles from "./LastWorkspaceActivitiesList.module.css";

interface LastWorkspaceActivitiesListProps {
  workspaceId: string;
}

const LastWorkspaceActivitiesList: React.FC<LastWorkspaceActivitiesListProps> = ({ workspaceId }) => {
  const [page, setPage] = useState<number>(0);
  // const { data: response, isLoading, isFetching } = useRetrieveActivitiesQuery({ workspaceId, page });

  const { data: response, isLoading, isFetching } = useFilterWorkspaceActivitiesQuery({ workspaceId, page });

  return (
    <div className={styles.container}>
      <PaginatedActivityList
        id={"last-workspace-activities-list"}
        name={""}
        response={response}
        isFetching={isFetching}
        isLoading={isLoading}
        page={page}
        setPage={setPage}
      />
    </div>
  );
};

export default LastWorkspaceActivitiesList;
