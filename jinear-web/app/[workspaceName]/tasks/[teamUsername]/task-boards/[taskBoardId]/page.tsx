"use client";
import TaskBoardDetailBreadcrumb from "@/components/taskBoardDetailScreen/taskBoardDetailBreadcrumb/TaskBoardDetailBreadcrumb";
import TaskBoardElementList from "@/components/taskLists/taskBoardList/taskBoardElementList/TaskBoardElementList";
import { useRetrieveTaskBoardQuery } from "@/store/api/taskBoardRetrieveApi";
import { useRetrieveWorkspaceTeamsQuery } from "@/store/api/teamApi";
import { selectWorkspaceFromWorkspaceUsername } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { CircularProgress } from "@mui/material";
import { useParams } from "next/navigation";
import React from "react";
import styles from "./index.module.css";

interface TaskBoardDetailScreenProps {}

const TaskBoardDetailScreen: React.FC<TaskBoardDetailScreenProps> = ({}) => {
  const params = useParams();
  const taskBoardId: string = params?.taskBoardId as string;
  const workspaceName: string = params?.workspaceName as string;
  const teamUsername: string = params?.teamUsername as string;

  const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));
  const { data: teamsResponse, isFetching: isTeamsFetching } = useRetrieveWorkspaceTeamsQuery(workspace?.workspaceId || "", {
    skip: workspace == null,
  });
  const team = teamsResponse?.data.find((teamDto) => teamDto.username == teamUsername);

  const {
    data: taskBoardResponse,
    isLoading,
    isFetching,
  } = useRetrieveTaskBoardQuery({ taskBoardId }, { skip: taskBoardId == null });

  return (
    <div className={styles.container}>
      {workspace && team && taskBoardResponse && (
        <TaskBoardDetailBreadcrumb
          workspace={workspace}
          team={team}
          title={taskBoardResponse.data.title}
          taskBoardId={taskBoardId}
        />
      )}
      {(isLoading || isFetching) && (
        <div className={styles.loadingContainer}>
          <CircularProgress size={14} />
        </div>
      )}
      {team && workspace && taskBoardResponse && (
        <TaskBoardElementList
          title={taskBoardResponse.data.title}
          taskBoardId={taskBoardResponse.data.taskBoardId}
          boardState={taskBoardResponse.data.state}
          dueDate={taskBoardResponse.data.dueDate}
          team={team}
          workspace={workspace}
        />
      )}
    </div>
  );
};

export default TaskBoardDetailScreen;
