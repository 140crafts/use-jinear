"use client";
import CircularLoading from "@/components/circularLoading/CircularLoading";
import MultiViewTaskList from "@/components/taskLists/multiViewTaskList/MultiViewTaskList";
import { useRetrieveWorkspaceTeamsQuery } from "@/store/api/teamApi";
import { selectWorkspaceFromWorkspaceUsername } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import useTranslation from "locales/useTranslation";
import { useParams, usePathname } from "next/navigation";
import React from "react";
import styles from "./index.module.css";
import { TaskDisplayFormat } from "@/components/taskLists/taskListTitleAndViewType/TaskListTitleAndViewType";

interface TasksScreenProps {
}

const getTeamDefaultDisplayFormat = (teamUsername: string): TaskDisplayFormat => {
  const defaultViewFormat = "WFS_COLUMN";
  if (typeof window === "object") {
    const viewFormat = localStorage.getItem(`df-${teamUsername}`) || defaultViewFormat;
    return ["LIST", "WFS_COLUMN"].indexOf(viewFormat) != -1 ? viewFormat as TaskDisplayFormat : defaultViewFormat;
  }
  return defaultViewFormat;
};

const TasksScreen: React.FC<TasksScreenProps> = ({}) => {
  const { t } = useTranslation();
  const params = useParams();
  const pathname = usePathname();
  const workspaceName: string = params?.workspaceName as string;
  const teamUsername: string = params?.teamUsername as string;

  const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));
  const {
    data: teamsResponse,
    isFetching: isTeamsFetching
  } = useRetrieveWorkspaceTeamsQuery(workspace?.workspaceId || "", {
    skip: workspace == null
  });
  const team = teamsResponse?.data.find((teamDto) => teamDto.username == teamUsername);
  const displayFormat = getTeamDefaultDisplayFormat(teamUsername);

  return (
    <div className={styles.container}>
      {isTeamsFetching && <CircularLoading />}
      {workspace && team && !isTeamsFetching && (
        <MultiViewTaskList
          title={t("tasksScreenBreadcrumbLabel")}
          workspace={workspace}
          team={team}
          activeDisplayFormat={displayFormat}
          workflowStatusBoardClassName={styles.workflowStatusBoard}
          pathname={pathname}
        />
      )}
    </div>
  );
};

export default TasksScreen;
