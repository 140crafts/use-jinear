import type {TaskDto, TeamWorkflowStatusDto} from "@/model/be/jinear-core";
import Logger from "@/util/logger";
import cn from "classnames";
import React, {useState} from "react";
import type {IWorkflowStatusUpdatePendingTask} from "../TeamWorkflowStatusBoard";
import ColumnTitle from "./columnTitle/ColumnTitle";
import StatusBoardTaskCard from "./statusBoardTaskCard/StatusBoardTaskCard";
import styles from "./WorkflowStatusColumn.module.css";

interface WorkflowStatusColumnProps {
  workflowStatusDto: TeamWorkflowStatusDto;
  tasks?: TaskDto[];
  workflowStatusUpdatePendingTask?: IWorkflowStatusUpdatePendingTask;
  onTaskDrop?: (taskId: string, newWorkflowStatusId: string, dropIndex?: number) => void;
  draggingTaskId?: string | null;
  onDragStart?: (taskId: string) => void;
  onDragEnd?: () => void;
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
      workflowStatusUpdatePendingTask
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
                                                                     onTaskDrop,
                                                                     draggingTaskId,
                                                                     onDragStart,
                                                                     onDragEnd
                                                                   }) => {
  const [isDragOver, setIsDragOver] = useState(false);
  const [dragOverIndex, setDragOverIndex] = useState<number | null>(null);

  const filteredTasks =
    tasks?.filter((taskDto) => filterByGroup(taskDto, workflowStatusDto, workflowStatusUpdatePendingTask)) || [];

  const handleDragOver = (e: React.DragEvent<HTMLDivElement>) => {
    e.preventDefault();
    e.dataTransfer.dropEffect = "move";
    setIsDragOver(true);
  };

  const handleDragLeave = (e: React.DragEvent<HTMLDivElement>) => {
    // Only reset if leaving the container entirely
    if (!e.currentTarget.contains(e.relatedTarget as Node)) {
      setIsDragOver(false);
      setDragOverIndex(null);
    }
  };

  const handleCardDragOver = (e: React.DragEvent<HTMLDivElement>, index: number) => {
    e.preventDefault();
    e.stopPropagation();
    setDragOverIndex(index);
  };

  const handleCardDragLeave = () => {
  };

  const handleDrop = (e: React.DragEvent<HTMLDivElement>) => {
    e.preventDefault();
    const taskId = e.dataTransfer.getData("text/plain");
    setIsDragOver(false);
    setDragOverIndex(null);
    onTaskDrop?.(taskId, workflowStatusDto.teamWorkflowStatusId, dragOverIndex ?? undefined);
  };

  const handleContainerDrop = (e: React.DragEvent<HTMLDivElement>) => {
    e.preventDefault();
    const taskId = e.dataTransfer.getData("text/plain");
    setIsDragOver(false);
    setDragOverIndex(null);
    onTaskDrop?.(taskId, workflowStatusDto.teamWorkflowStatusId, filteredTasks.length);
  };

  return (
    <div className={styles.container}>
      <ColumnTitle workflowStatusDto={workflowStatusDto} />
      <div
        className={cn(
          styles.contentContainer,
          filteredTasks?.length === 0 ? styles.gradientBg : undefined,
          { [styles.dragOver]: isDragOver }
        )}
        onDragOver={handleDragOver}
        onDragLeave={handleDragLeave}
        onDrop={handleContainerDrop}
      >
        {filteredTasks.map((taskDto, index) => {
          const isDragging = draggingTaskId === taskDto.taskId;
          const isBeforeDropTarget = dragOverIndex !== null && index >= dragOverIndex && !isDragging;
          return (
            <div
              key={`card-wrapper-${taskDto.taskId}`}
              className={cn(styles.cardWrapper, {
                [styles.shiftDown]: isBeforeDropTarget
              })}
              onDragOver={(e) => handleCardDragOver(e, index)}
              onDragLeave={handleCardDragLeave}
              onDrop={handleDrop}
            >
              <StatusBoardTaskCard
                task={taskDto}
                index={index}
                isDragging={isDragging}
                onDragStart={() => onDragStart?.(taskDto.taskId)}
                onDragEnd={onDragEnd}
              />
            </div>
          );
        })}
      </div>
    </div>
  );
};

export default WorkflowStatusColumn;
