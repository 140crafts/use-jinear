import { TaskDto, TeamWorkflowStatusDto } from "@/model/be/jinear-core";
import Logger from "@/utils/logger";
import cn from "classnames";
import React from "react";
import { Droppable, DroppableProvided, DroppableStateSnapshot } from "react-beautiful-dnd";
import { IWorkflowStatusUpdatePendingTask } from "../TeamWorkflowStatusBoard";
import ColumnTitle from "./columnTitle/ColumnTitle";
import StatusBoardTaskCard from "./statusBoardTaskCard/StatusBoardTaskCard";
import styles from "./WorkflowStatusColumn.module.css";

interface WorkflowStatusColumnProps {
  workflowStatusDto: TeamWorkflowStatusDto;
  tasks?: TaskDto[];
  workflowStatusUpdatePendingTask?: IWorkflowStatusUpdatePendingTask;
}
const filterByGroup = (
  taskDto: TaskDto,
  workflowStatusDto: TeamWorkflowStatusDto,
  workflowStatusUpdatePendingTask?: IWorkflowStatusUpdatePendingTask
) => {
  const isThisTaskStatusUpdatePendingTask = workflowStatusUpdatePendingTask?.taskId == taskDto.taskId;
  const taskIsInThisGroup = taskDto.workflowStatus.workflowStateGroup == workflowStatusDto.workflowStateGroup;
  if (isThisTaskStatusUpdatePendingTask) {
    logger.log({
      msg: "STATUS UPDATE PENDING TASK",
      taskDto,
      workflowStatusDto,
      workflowStatusUpdatePendingTask,
    });
    return workflowStatusDto.teamWorkflowStatusId == workflowStatusUpdatePendingTask.newWorkflowStatusId;
  }
  return taskIsInThisGroup;
};

const logger = Logger("WorkflowStatusColumn");

const WorkflowStatusColumn: React.FC<WorkflowStatusColumnProps> = ({
  workflowStatusDto,
  tasks,
  workflowStatusUpdatePendingTask,
}) => {
  const filteredTasks =
    tasks?.filter((taskDto) => filterByGroup(taskDto, workflowStatusDto, workflowStatusUpdatePendingTask)) || [];

  return (
    <div className={styles.container}>
      <ColumnTitle workflowStatusDto={workflowStatusDto} />
      <Droppable key={workflowStatusDto.teamWorkflowStatusId} droppableId={workflowStatusDto.teamWorkflowStatusId}>
        {(provided: DroppableProvided, snapshot: DroppableStateSnapshot): JSX.Element => (
          <div
            ref={provided.innerRef}
            {...provided.droppableProps}
            className={cn(styles.contentContainer, filteredTasks?.length == 0 ? styles.gradientBg : undefined)}
          >
            {filteredTasks.map((taskDto, index) => (
              <StatusBoardTaskCard key={`status-board-task-card-${taskDto.taskId}`} task={taskDto} index={index} />
            ))}
            {provided.placeholder}
          </div>
        )}
      </Droppable>
    </div>
  );
};

export default WorkflowStatusColumn;
