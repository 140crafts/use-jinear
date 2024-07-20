"use client";
import AssignedToMeScreenHeader
  from "@/components/assignedToMeScreen/assignedToMeScreenHeader/AssignedToMeScreenHeader";
import PrefilteredPaginatedTaskList
  from "@/components/taskLists/prefilteredPaginatedTaskList/PrefilteredPaginatedTaskList";
import { TeamDto } from "@/model/be/jinear-core";
import { selectCurrentAccountId, selectWorkspaceFromWorkspaceUsername } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { useParams } from "next/navigation";
import React, { useState } from "react";
import styles from "./index.module.css";

interface AssignedToMePageProps {
}

const AssignedToMePage: React.FC<AssignedToMePageProps> = ({}) => {
  const currentAccountId = useTypedSelector(selectCurrentAccountId);
  const params = useParams();
  const workspaceName: string = params?.workspaceName as string;
  const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));
  const [filterBy, setFilterBy] = useState<TeamDto>();

  return (
    <div className={styles.container}>
      {workspace && <AssignedToMeScreenHeader filterBy={filterBy} setFilterBy={setFilterBy} workspace={workspace} />}
      {workspace && currentAccountId && (
        <PrefilteredPaginatedTaskList
          id={"assigned-to-me-task-list"}
          filter={{
            workspaceId: workspace.workspaceId,
            assigneeIds: [currentAccountId],
            teamIdList: filterBy ? [filterBy.teamId] : undefined
          }}
        />
      )}
    </div>
  );
};

export default AssignedToMePage;
