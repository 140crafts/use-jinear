import AssignedToMeScreenHeader from "@/components/assignedToMeScreen/assignedToMeScreenHeader/AssignedToMeScreenHeader";
import PaginatedAssignedToCurrentAccountTaskList from "@/components/taskLists/paginatedAssignedToCurrentAccountTaskList/PaginatedAssignedToCurrentAccountTaskList";
import PaginatedFromTeamWithAssigneeTaskList from "@/components/taskLists/paginatedFromTeamWithAssigneeTaskList/PaginatedFromTeamWithAssigneeTaskList";
import { TeamDto } from "@/model/be/jinear-core";
import {
  selectCurrentAccountId,
  selectCurrentAccountsPreferredTeamId,
  selectCurrentAccountsPreferredWorkspaceId,
} from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import React, { useState } from "react";
import styles from "./index.module.css";

interface AssignedToMePageProps {}

const AssignedToMePage: React.FC<AssignedToMePageProps> = ({}) => {
  const currentAccountId = useTypedSelector(selectCurrentAccountId);
  const currentWorkspaceId = useTypedSelector(selectCurrentAccountsPreferredWorkspaceId);
  const currentTeamId = useTypedSelector(selectCurrentAccountsPreferredTeamId);
  const [filterBy, setFilterBy] = useState<TeamDto>();

  return (
    <div className={styles.container}>
      {currentWorkspaceId && (
        <AssignedToMeScreenHeader filterBy={filterBy} setFilterBy={setFilterBy} workspaceId={currentWorkspaceId} />
      )}
      {currentWorkspaceId &&
        (filterBy ? (
          <PaginatedFromTeamWithAssigneeTaskList
            workspaceId={currentWorkspaceId}
            teamId={filterBy.teamId || ""}
            assigneeId={currentAccountId || ""}
          />
        ) : (
          <PaginatedAssignedToCurrentAccountTaskList workspaceId={currentWorkspaceId} />
        ))}
    </div>
  );
};

export default AssignedToMePage;
