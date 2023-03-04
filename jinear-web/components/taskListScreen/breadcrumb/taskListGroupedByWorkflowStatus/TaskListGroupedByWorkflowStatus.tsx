import { useRetrieveAllFromTeamQuery } from "@/store/api/teamWorkflowStatusApi";
import React from "react";
import styles from "./TaskListGroupedByWorkflowStatus.module.css";
import WorkflowTaskList from "./workflowTaskList/WorkflowTaskList";

interface TaskListGroupedByWorkflowStatusProps {
  workspaceId: string;
  teamId: string;
  type: "active" | "archive" | "backlog";
}

const TaskListGroupedByWorkflowStatus: React.FC<TaskListGroupedByWorkflowStatusProps> = ({ workspaceId, teamId, type }) => {
  const { data: teamWorkflowListData } = useRetrieveAllFromTeamQuery({
    teamId,
  });
  return (
    <div className={styles.container}>
      {type == "backlog" &&
        teamWorkflowListData?.data.groupedTeamWorkflowStatuses.BACKLOG?.map((workflowDto) => (
          <WorkflowTaskList
            key={`workflow-task-list-${workflowDto.teamWorkflowStatusId}`}
            workspaceId={workspaceId}
            teamId={teamId}
            workflowStatusId={workflowDto.teamWorkflowStatusId}
            name={workflowDto.name}
          />
        ))}
      {type == "active" &&
        teamWorkflowListData?.data.groupedTeamWorkflowStatuses.NOT_STARTED?.map((workflowDto) => (
          <WorkflowTaskList
            key={`workflow-task-list-${workflowDto.teamWorkflowStatusId}`}
            workspaceId={workspaceId}
            teamId={teamId}
            workflowStatusId={workflowDto.teamWorkflowStatusId}
            name={workflowDto.name}
          />
        ))}
      {type == "active" &&
        teamWorkflowListData?.data.groupedTeamWorkflowStatuses.STARTED?.map((workflowDto) => (
          <WorkflowTaskList
            key={`workflow-task-list-${workflowDto.teamWorkflowStatusId}`}
            workspaceId={workspaceId}
            teamId={teamId}
            workflowStatusId={workflowDto.teamWorkflowStatusId}
            name={workflowDto.name}
          />
        ))}
      {type == "archive" &&
        teamWorkflowListData?.data.groupedTeamWorkflowStatuses.COMPLETED?.map((workflowDto) => (
          <WorkflowTaskList
            key={`workflow-task-list-${workflowDto.teamWorkflowStatusId}`}
            workspaceId={workspaceId}
            teamId={teamId}
            workflowStatusId={workflowDto.teamWorkflowStatusId}
            name={workflowDto.name}
          />
        ))}
      {type == "archive" &&
        teamWorkflowListData?.data.groupedTeamWorkflowStatuses.CANCELLED?.map((workflowDto) => (
          <WorkflowTaskList
            key={`workflow-task-list-${workflowDto.teamWorkflowStatusId}`}
            workspaceId={workspaceId}
            teamId={teamId}
            workflowStatusId={workflowDto.teamWorkflowStatusId}
            name={workflowDto.name}
          />
        ))}
    </div>
  );
};

export default TaskListGroupedByWorkflowStatus;
