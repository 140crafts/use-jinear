import { useRetrieveAllIntersectingTasksQuery } from "@/store/api/taskListingApi";
import { useRetrieveAllFromTeamQuery } from "@/store/api/teamWorkflowStatusApi";
import { CircularProgress } from "@mui/material";
import React from "react";
import styles from "./TeamWorkflowStatusBoard.module.css";
import WorkflowStatusColumn from "./workflowStatusColumn/WorkflowStatusColumn";

interface TeamWorkflowStatusBoardProps {
  workspaceId: string;
  teamId: string;
  startDate: Date;
  endDate: Date;
}

const TeamWorkflowStatusBoard: React.FC<TeamWorkflowStatusBoardProps> = ({
  teamId,
  workspaceId,
  startDate,
  endDate,
}) => {
  const {
    data: taskListingResponse,
    isFetching: isTaskListingFetching,
    isSuccess: isTaskListingSuccess,
  } = useRetrieveAllIntersectingTasksQuery(
    {
      teamId,
      workspaceId,
      timespanStart: startDate,
      timespanEnd: endDate,
    },
    { skip: teamId == null || workspaceId == null }
  );
  const { data: teamWorkflowListData, isFetching: isTeamWorkflowListFetching } =
    useRetrieveAllFromTeamQuery({ teamId }, { skip: teamId == null });

  return (
    <div className={styles.container}>
      {(isTaskListingFetching || isTeamWorkflowListFetching) && (
        <div className={styles.loading}>
          <CircularProgress size={24} />
        </div>
      )}
      {teamWorkflowListData?.data.groupedTeamWorkflowStatuses.BACKLOG?.map(
        (workflowDto) => (
          <WorkflowStatusColumn
            key={workflowDto.teamWorkflowStatusId}
            workflowStatusDto={workflowDto}
            tasks={taskListingResponse?.data}
          />
        )
      )}
      {teamWorkflowListData?.data.groupedTeamWorkflowStatuses.NOT_STARTED?.map(
        (workflowDto) => (
          <WorkflowStatusColumn
            key={workflowDto.teamWorkflowStatusId}
            workflowStatusDto={workflowDto}
            tasks={taskListingResponse?.data}
          />
        )
      )}
      {teamWorkflowListData?.data.groupedTeamWorkflowStatuses.STARTED?.map(
        (workflowDto) => (
          <WorkflowStatusColumn
            key={workflowDto.teamWorkflowStatusId}
            workflowStatusDto={workflowDto}
            tasks={taskListingResponse?.data}
          />
        )
      )}
      {teamWorkflowListData?.data.groupedTeamWorkflowStatuses.COMPLETED?.map(
        (workflowDto) => (
          <WorkflowStatusColumn
            key={workflowDto.teamWorkflowStatusId}
            workflowStatusDto={workflowDto}
            tasks={taskListingResponse?.data}
          />
        )
      )}
      {teamWorkflowListData?.data.groupedTeamWorkflowStatuses.CANCELLED?.map(
        (workflowDto) => (
          <WorkflowStatusColumn
            key={workflowDto.teamWorkflowStatusId}
            workflowStatusDto={workflowDto}
            tasks={taskListingResponse?.data}
          />
        )
      )}
    </div>
  );
};

export default TeamWorkflowStatusBoard;
