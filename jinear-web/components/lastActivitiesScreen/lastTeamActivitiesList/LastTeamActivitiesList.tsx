import { useRetrieveActivitiesFromTeamQuery } from "@/store/api/workspaceActivityApi";
import React, { useState } from "react";
import PaginatedActivityList from "../paginatedActivityList/PaginatedActivityList";
import styles from "./LastTeamActivitiesList.module.css";

interface LastTeamActivitiesListProps {
  workspaceId: string;
  teamId: string;
}

const LastTeamActivitiesList: React.FC<LastTeamActivitiesListProps> = ({ workspaceId, teamId }) => {
  const [page, setPage] = useState<number>(0);
  const { data: response, isLoading, isFetching } = useRetrieveActivitiesFromTeamQuery({ workspaceId, teamId, page });

  return (
    <div className={styles.container}>
      <PaginatedActivityList
        id={"last-workspace-team-activities-list"}
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

export default LastTeamActivitiesList;
