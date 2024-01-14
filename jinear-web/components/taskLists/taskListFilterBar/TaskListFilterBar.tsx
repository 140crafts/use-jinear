import { TaskFilterRequest, TeamDto, WorkspaceDto } from "@/model/be/jinear-core";

import Logger from "@/utils/logger";
import React from "react";
import styles from "./TaskListFilterBar.module.css";
import AssigneeFilterButton from "./assigneeFilterButton/AssigneeFilterButton";
import TaskListFilterBarContext from "./context/TaskListFilterBarContext";
import FromDatePickerButton from "./fromDatePickerButton/FromDatePickerButton";
import OwnerFilterButton from "./ownerFilterButton/OwnerFilterButton";
import QuickFilterBar from "./quickFilterBar/QuickFilterBar";
import ToDatePickerButton from "./toDatePickerButton/ToDatePickerButton";
import TopicFilterButton from "./topicFilterButton/TopicFilterButton";
import WorkflowStatusFilterButton from "./workflowStatusFilterButton/WorkflowStatusFilterButton";
interface TaskListFilterBarProps {
  workspace: WorkspaceDto;
  team: TeamDto;
  onFilterChange?: (filter: TaskFilterRequest) => void;
}

const logger = Logger("TaskListFilterBar");

const TaskListFilterBar: React.FC<TaskListFilterBarProps> = ({ workspace, team }) => {
  return (
    <TaskListFilterBarContext.Provider
      value={{
        team,
        workspace,
      }}
    >
      <>
        <div className={styles.container}>
          <FromDatePickerButton />
          <ToDatePickerButton />
          <WorkflowStatusFilterButton />
          <AssigneeFilterButton />
          <OwnerFilterButton />
          <TopicFilterButton />
        </div>
        <QuickFilterBar />
      </>
    </TaskListFilterBarContext.Provider>
  );
};

export default React.memo(TaskListFilterBar, (prevProps, nextProps) => {
  const prev = JSON.stringify(prevProps);
  const next = JSON.stringify(nextProps);
  const areEqual = prev == next;
  logger.log({ prevProps, nextProps, areEqual });
  return areEqual;
});
