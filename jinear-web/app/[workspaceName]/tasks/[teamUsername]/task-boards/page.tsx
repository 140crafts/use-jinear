"use client";
import TaskListListBreadCrumb from "@/components/taskBoardsListScreen/taskBoardListBreadCrumb/TaskBoardListBreadCrumb";
import TaskListList from "@/components/taskLists/taskBoardList/TaskBoardList";
import { useRetrieveWorkspaceTeamsQuery } from "@/store/api/teamApi";
import { selectWorkspaceFromWorkspaceUsername } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { useParams } from "next/navigation";
import React from "react";
import styles from "./index.module.css";

interface TaskBoardListScreenProps {}

const TaskBoardListScreen: React.FC<TaskBoardListScreenProps> = ({}) => {
  const params = useParams();
  const workspaceName: string = params?.workspaceName as string;
  const teamUsername: string = params?.teamUsername as string;

  const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));
  const { data: teamsResponse, isFetching: isTeamsFetching } = useRetrieveWorkspaceTeamsQuery(workspace?.workspaceId || "", {
    skip: workspace == null,
  });
  const team = teamsResponse?.data.find((teamDto) => teamDto.username == teamUsername);

  return (
    <div className={styles.container}>
      {workspace && team && <TaskListListBreadCrumb workspace={workspace} team={team} />}
      {team && workspace && <TaskListList team={team} workspace={workspace} />}
    </div>
  );
};

export default TaskBoardListScreen;
