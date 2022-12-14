import { TaskDto, TeamWorkflowStatusDto } from "@/model/be/jinear-core";
import cn from "classnames";
import React from "react";
import ColumnTitle from "./columnTitle/ColumnTitle";
import StatusBoardTaskCard from "./statusBoardTaskCard/StatusBoardTaskCard";
import styles from "./WorkflowStatusColumn.module.css";

interface WorkflowStatusColumnProps {
  workflowStatusDto: TeamWorkflowStatusDto;
  tasks?: TaskDto[];
}
const filterByGroup = (
  taskDto: TaskDto,
  workflowStatusDto: TeamWorkflowStatusDto
) =>
  taskDto.workflowStatus.workflowStateGroup ==
  workflowStatusDto.workflowStateGroup;
const WorkflowStatusColumn: React.FC<WorkflowStatusColumnProps> = ({
  workflowStatusDto,
  tasks,
}) => {
  const filteredTasks =
    tasks?.filter((taskDto) => filterByGroup(taskDto, workflowStatusDto)) || [];
  return (
    <div className={styles.container}>
      <ColumnTitle workflowStatusDto={workflowStatusDto} />
      <div
        className={cn(
          styles.contentContainer,
          filteredTasks?.length == 0 ? styles.gradientBg : undefined
        )}
      >
        {filteredTasks.map((taskDto) => (
          <StatusBoardTaskCard
            key={`status-board-task-card-${taskDto.taskId}`}
            task={taskDto}
          />
        ))}
      </div>
    </div>
  );
};

export default WorkflowStatusColumn;
