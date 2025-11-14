import React from "react";
import styles from "./TaskBoard.module.css";
import TaskBoardTitle from "@/components/taskLists/taskBoardList/taskBoard/taskBoardTitle/TaskBoardTitle";
import { TaskBoardDto, TaskBoardStateType, TeamDto, WorkspaceDto } from "@/be/jinear-core";
import TaskBoardElementList
  from "@/components/taskLists/taskBoardList/taskBoard/taskBoardElementList/TaskBoardElementList";
import TaskBoardColumnView
  from "@/components/taskLists/taskBoardList/taskBoard/taskBoardColumnView/TaskBoardColumnView";
import { useQueryState } from "@/hooks/useQueryState";
import { TaskDisplayFormat } from "@/components/taskLists/taskListTitleAndViewType/TaskListTitleAndViewType";

interface TaskBoardProps {
  taskBoard: TaskBoardDto;
  team: TeamDto;
  workspace: WorkspaceDto;
  staticViewType?: "list" | "column";
}

const getTaskBoardDefaultDisplayFormat = (taskBoardId: string): "list" | "column" => {
  const defaultViewFormat = "list";
  if (typeof window === "object") {
    const viewFormat = localStorage.getItem(`task-board-view-${taskBoardId}`) || defaultViewFormat;
    return ["list", "column"].indexOf(viewFormat) != -1 ? viewFormat as ("list" | "column") : defaultViewFormat;
  }
  return defaultViewFormat;
};

export const setTaskBoardDefaultDisplayFormat = (taskBoardId: string, format: "list" | "column") => {
  if (typeof window === "object") {
    localStorage.setItem(`task-board-view-${taskBoardId}`, format);
  }
};

const TaskBoard: React.FC<TaskBoardProps> = ({
                                               taskBoard,
                                               workspace,
                                               team,
                                               staticViewType
                                             }) => {
  const displayFormat = useQueryState<"list" | "column">("displayFormat") || getTaskBoardDefaultDisplayFormat(taskBoard.taskBoardId);

  return (
    <div className={styles.container}>
      <TaskBoardTitle
        taskBoard={taskBoard}
        team={team}
        workspace={workspace}
        displayFormat={displayFormat}
        noDisplayFormatChange={staticViewType != null}
      />
      <TaskBoardElementList
        taskBoardId={taskBoard.taskBoardId}
        boardState={taskBoard.state}
        className={(staticViewType == "list" || (staticViewType == undefined && displayFormat == "list")) ? styles.visible : styles.hidden}
      />
      <TaskBoardColumnView
        taskBoardId={taskBoard.taskBoardId}
        teamId={team.teamId}
        className={(staticViewType == "column" || (staticViewType == undefined && displayFormat == "column")) ? styles.visible : styles.hidden}
      />
    </div>
  );
};

export default TaskBoard;