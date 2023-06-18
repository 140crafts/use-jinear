import TeamWorkflowStatusBoard from "@/components/teamWorkflowStatusBoard/TeamWorkflowStatusBoard";
import { useRetrieveAllIntersectingTasksFromTeamQuery } from "@/store/api/taskListingApi";
import React from "react";
import styles from "./WorkflowStatusTab.module.scss";

interface WorkflowStatusTabProps {
  workspaceId?: string;
  teamId?: string;
  startDate: Date;
  endDate: Date;
}

const WorkflowStatusTab: React.FC<WorkflowStatusTabProps> = ({ workspaceId = "", teamId = "", startDate, endDate }) => {
  const { data: taskListingResponse, isFetching: isTaskListingFetching } = useRetrieveAllIntersectingTasksFromTeamQuery(
    {
      teamId,
      workspaceId,
      timespanStart: startDate,
      timespanEnd: endDate,
    },
    { skip: teamId == null || teamId == "" || workspaceId == null || workspaceId == "" }
  );

  return (
    <div className={styles.container}>
      <div className={styles.actionBarContainer}></div>
      <span className={styles.statusBoardContainer}>
        {workspaceId && teamId && (
          <TeamWorkflowStatusBoard
            teamId={teamId}
            taskList={taskListingResponse?.data || []}
            isTaskListingLoading={isTaskListingFetching}
          />
        )}
      </span>
    </div>
  );
};

export default WorkflowStatusTab;
