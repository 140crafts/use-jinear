import AssignedToMeScreenHeader from "@/components/assignedToMeScreen/assignedToMeScreenHeader/AssignedToMeScreenHeader";
import PaginatedAssignedToCurrentAccountTaskList from "@/components/taskLists/paginatedAssignedToCurrentAccountTaskList/PaginatedAssignedToCurrentAccountTaskList";
import PaginatedFromTeamWithAssigneeTaskList from "@/components/taskLists/paginatedFromTeamWithAssigneeTaskList/PaginatedFromTeamWithAssigneeTaskList";
import { TeamDto } from "@/model/be/jinear-core";
import { selectCurrentAccountId, selectWorkspaceFromWorkspaceUsername } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { useRouter } from "next/router";
import React, { useState } from "react";
import styles from "./index.module.css";

interface AssignedToMePageProps {}

const AssignedToMePage: React.FC<AssignedToMePageProps> = ({}) => {
  const currentAccountId = useTypedSelector(selectCurrentAccountId);
  const router = useRouter();
  const workspaceName: string = router.query?.workspaceName as string;
  const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));
  const [filterBy, setFilterBy] = useState<TeamDto>();

  return (
    <div className={styles.container}>
      {workspace && <AssignedToMeScreenHeader filterBy={filterBy} setFilterBy={setFilterBy} workspace={workspace} />}
      {workspace &&
        (filterBy ? (
          <PaginatedFromTeamWithAssigneeTaskList
            workspaceId={workspace.workspaceId}
            teamId={filterBy.teamId || ""}
            assigneeId={currentAccountId || ""}
          />
        ) : (
          <PaginatedAssignedToCurrentAccountTaskList workspaceId={workspace.workspaceId} />
        ))}
    </div>
  );
};

export default AssignedToMePage;
