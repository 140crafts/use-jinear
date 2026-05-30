import { useFilterWorkspaceActivitiesQuery } from "@/store/api/workspaceActivityApi";
import React, { useState } from "react";
import PaginatedActivityList from "../paginatedActivityList/PaginatedActivityList";
import styles from "./LastTeamActivitiesList.module.css";

interface LastTeamActivitiesListProps {
  workspaceId: string;
  teamId: string;
  size?: number;
  contentContainerClassName?: string;
}

const LastTeamActivitiesList: React.FC<LastTeamActivitiesListProps> = ({
  workspaceId,
  teamId,
  size = 25,
  contentContainerClassName,
}) => {
  const [page, setPage] = useState<number>(0);
  const {
    data: response,
    isLoading,
    isFetching,
  } = useFilterWorkspaceActivitiesQuery({ workspaceId, teamIdList: [teamId], page, size });

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
        contentContainerClassName={contentContainerClassName}
      />
    </div>
  );
};

export default LastTeamActivitiesList;
