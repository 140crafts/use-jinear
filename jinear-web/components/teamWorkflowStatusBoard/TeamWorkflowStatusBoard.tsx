import { useRetrieveAllIntersectingTasksFromTeamQuery } from "@/store/api/taskListingApi";
import { useUpdateTaskWorkflowStatusMutation } from "@/store/api/taskWorkflowStatusApi";
import { useRetrieveAllFromTeamQuery } from "@/store/api/teamWorkflowStatusApi";
import Logger from "@/utils/logger";
import cn from "classnames";
import React, { useEffect, useState } from "react";
import { DragDropContext, DropResult } from "react-beautiful-dnd";
import styles from "./TeamWorkflowStatusBoard.module.css";
import WorkflowStatusColumn from "./workflowStatusColumn/WorkflowStatusColumn";

export interface IWorkflowStatusUpdatePendingTask {
  taskId: string;
  newWorkflowStatusId: string;
}

interface TeamWorkflowStatusBoardProps {
  workspaceId: string;
  teamId: string;
  startDate: Date;
  endDate: Date;
}
const logger = Logger("TeamWorkflowStatusBoard");

const TeamWorkflowStatusBoard: React.FC<TeamWorkflowStatusBoardProps> = ({ teamId, workspaceId, startDate, endDate }) => {
  const {
    data: taskListingResponse,
    isFetching: isTaskListingFetching,
    isSuccess: isTaskListingSuccess,
  } = useRetrieveAllIntersectingTasksFromTeamQuery(
    {
      teamId,
      workspaceId,
      timespanStart: startDate,
      timespanEnd: endDate,
    },
    { skip: teamId == null || workspaceId == null }
  );

  const { data: teamWorkflowListData, isFetching: isTeamWorkflowListFetching } = useRetrieveAllFromTeamQuery(
    { teamId },
    { skip: teamId == null }
  );

  const [updateTaskWorkflowStatus, { isLoading }] = useUpdateTaskWorkflowStatusMutation();

  const [workflowStatusUpdatePendingTask, setWorkflowStatusUpdatePendingTask] = useState<IWorkflowStatusUpdatePendingTask>();

  useEffect(() => {
    if (!isTaskListingFetching) {
      setWorkflowStatusUpdatePendingTask(undefined);
    }
  }, [isTaskListingFetching]);

  const onDragEnd = (result: DropResult) => {
    const { source, destination } = result;
    if (!destination || source.droppableId == destination?.droppableId) {
      return;
    }
    const workflowStatusId = destination.droppableId;
    const taskId = result.draggableId;
    logger.log({ taskId, workflowStatusId });
    setWorkflowStatusUpdatePendingTask({
      taskId,
      newWorkflowStatusId: workflowStatusId,
    });
    updateTaskWorkflowStatus({ taskId, workflowStatusId });
  };

  const _isLoading = isTaskListingFetching || isTeamWorkflowListFetching || workflowStatusUpdatePendingTask;

  return (
    <div className={cn(styles.container, _isLoading ? styles["container-disabled"] : undefined)}>
      <DragDropContext onDragEnd={onDragEnd}>
        {teamWorkflowListData?.data.groupedTeamWorkflowStatuses.BACKLOG?.map((workflowDto) => (
          <WorkflowStatusColumn
            key={workflowDto.teamWorkflowStatusId}
            workflowStatusDto={workflowDto}
            tasks={taskListingResponse?.data}
            workflowStatusUpdatePendingTask={workflowStatusUpdatePendingTask}
          />
        ))}
        {teamWorkflowListData?.data.groupedTeamWorkflowStatuses.NOT_STARTED?.map((workflowDto) => (
          <WorkflowStatusColumn
            key={workflowDto.teamWorkflowStatusId}
            workflowStatusDto={workflowDto}
            tasks={taskListingResponse?.data}
            workflowStatusUpdatePendingTask={workflowStatusUpdatePendingTask}
          />
        ))}
        {teamWorkflowListData?.data.groupedTeamWorkflowStatuses.STARTED?.map((workflowDto) => (
          <WorkflowStatusColumn
            key={workflowDto.teamWorkflowStatusId}
            workflowStatusDto={workflowDto}
            tasks={taskListingResponse?.data}
            workflowStatusUpdatePendingTask={workflowStatusUpdatePendingTask}
          />
        ))}
        {teamWorkflowListData?.data.groupedTeamWorkflowStatuses.COMPLETED?.map((workflowDto) => (
          <WorkflowStatusColumn
            key={workflowDto.teamWorkflowStatusId}
            workflowStatusDto={workflowDto}
            tasks={taskListingResponse?.data}
            workflowStatusUpdatePendingTask={workflowStatusUpdatePendingTask}
          />
        ))}
        {teamWorkflowListData?.data.groupedTeamWorkflowStatuses.CANCELLED?.map((workflowDto) => (
          <WorkflowStatusColumn
            key={workflowDto.teamWorkflowStatusId}
            workflowStatusDto={workflowDto}
            tasks={taskListingResponse?.data}
            workflowStatusUpdatePendingTask={workflowStatusUpdatePendingTask}
          />
        ))}
      </DragDropContext>
    </div>
  );
};

export default TeamWorkflowStatusBoard;
