import React from "react";
import styles from "./TaskBoard.module.css";
import TaskBoardTitle from "@/components/taskLists/taskBoardList/taskBoard/taskBoardTitle/TaskBoardTitle";
import { TaskBoardDto, TaskBoardStateType, TeamDto, WorkspaceDto } from "@/be/jinear-core";
import TaskBoardElementList
  from "@/components/taskLists/taskBoardList/taskBoard/taskBoardElementList/TaskBoardElementList";
import TaskBoardColumnView
  from "@/components/taskLists/taskBoardList/taskBoard/taskBoardColumnView/TaskBoardColumnView";
import { useQueryState } from "@/hooks/useQueryState";

interface TaskBoardProps {
  taskBoard: TaskBoardDto;
  team: TeamDto;
  workspace: WorkspaceDto;
  staticViewType?: "list" | "column";
}

const TaskBoard: React.FC<TaskBoardProps> = ({
                                               taskBoard,
                                               workspace,
                                               team,
                                               staticViewType
                                             }) => {
  const displayFormat = useQueryState<"list" | "column">("displayFormat") || "column";

  return (
    <div className={styles.container}>
      <TaskBoardTitle
        taskBoard={taskBoard}
        team={team}
        workspace={workspace}
        displayFormat={displayFormat}
        noDisplayFormatChange={staticViewType != null}
      />
      <TaskBoardElementList taskBoardId={taskBoard.taskBoardId}
                            className={(staticViewType == "list" || (staticViewType == undefined && displayFormat == "list")) ? styles.visible : styles.hidden} />
      <TaskBoardColumnView taskBoardId={taskBoard.taskBoardId}
                           teamId={team.teamId}
                           className={(staticViewType == "column" || (staticViewType == undefined && displayFormat == "column")) ? styles.visible : styles.hidden} />
    </div>
  );
};

export default TaskBoard;