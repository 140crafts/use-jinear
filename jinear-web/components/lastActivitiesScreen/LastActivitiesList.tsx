import { TeamDto, WorkspaceDto } from "@/model/be/jinear-core";
import Logger from "@/utils/logger";
import React, { useState } from "react";
import styles from "./LastActivitiesList.module.css";
import LastActivitiesScreenHeader from "./lastActivitiesScreenHeader/LastActivitiesScreenHeader";
import LastTeamActivitiesList from "./lastTeamActivitiesList/LastTeamActivitiesList";
import LastWorkspaceActivitiesList from "./lastWorkspaceActivitiesList/LastWorkspaceActivitiesList";

interface LastActivitiesListProps {
  workspace: WorkspaceDto;
}

const logger = Logger("LastActivitiesList");

const LastActivitiesList: React.FC<LastActivitiesListProps> = ({ workspace }) => {
  const [filterBy, setFilterBy] = useState<TeamDto>();
  return (
    <div className={styles.container}>
      <LastActivitiesScreenHeader workspace={workspace} filterBy={filterBy} setFilterBy={setFilterBy} />
      <div className="spacer-h-2" />
      {filterBy ? (
        <LastTeamActivitiesList workspaceId={filterBy.workspaceId} teamId={filterBy.teamId} />
      ) : (
        <LastWorkspaceActivitiesList workspaceId={workspace.workspaceId} />
      )}
    </div>
  );
};

export default LastActivitiesList;
