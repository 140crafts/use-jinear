import TaskDescription from "@/components/taskDetail/taskDescription/TaskDescription";
import TaskOwnerInfo from "@/components/taskDetail/taskOwnerInfo/TaskOwnerInfo";
import TaskPageHeader from "@/components/taskDetail/taskPageHeader/TaskPageHeader";
import TaskTitle from "@/components/taskDetail/taskTitle/TaskTitle";
import TaskDetailBreadcrumb from "@/components/taskDetailBreadcrumb/TaskDetailBreadcrumb";
import { useRetrieveWithWorkspaceNameAndTeamTagNoQuery } from "@/store/api/taskApi";
import {
  useUpdatePreferredTeamMutation,
  useUpdatePreferredWorkspaceMutation,
} from "@/store/api/workspaceDisplayPreferenceApi";
import {
  selectCurrentAccountsPreferredTeamId,
  selectCurrentAccountsPreferredWorkspace,
} from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { CircularProgress } from "@mui/material";
import { useRouter } from "next/router";
import React, { useEffect } from "react";
import styles from "./index.module.css";

interface TaskDetailPageProps {}

const TaskDetailPage: React.FC<TaskDetailPageProps> = ({}) => {
  const router = useRouter();
  const currentWorkspace = useTypedSelector(
    selectCurrentAccountsPreferredWorkspace
  );
  const preferredTeamId = useTypedSelector(
    selectCurrentAccountsPreferredTeamId
  );
  const workspaceName: string = router.query?.workspaceName as string;
  const taskTag: string = router.query?.taskTag as string;

  const {
    data: taskResponse,
    isLoading: isTaskResponseLoading,
    isSuccess: isTaskResponseSuccess,
  } = useRetrieveWithWorkspaceNameAndTeamTagNoQuery(
    { workspaceName, taskTag },
    { skip: workspaceName == null || taskTag == null }
  );

  const [updatePreferredWorkspace] = useUpdatePreferredWorkspaceMutation();
  const [updatePreferredTeam] = useUpdatePreferredTeamMutation();

  useEffect(() => {
    if (
      currentWorkspace &&
      isTaskResponseSuccess &&
      taskResponse &&
      preferredTeamId
    ) {
      if (taskResponse.data.workspaceId != currentWorkspace.workspaceId) {
        updatePreferredWorkspace({
          workspaceId: taskResponse.data.workspaceId,
        });
      }
      if (preferredTeamId != taskResponse.data.teamId) {
        updatePreferredTeam({
          teamId: taskResponse.data.teamId,
          workspaceId: taskResponse.data.workspaceId,
        });
      }
    }
  }, [currentWorkspace, preferredTeamId, taskResponse, isTaskResponseSuccess]);

  return (
    <div className={styles.container}>
      {isTaskResponseLoading && (
        <div className={styles.loadingContainer}>
          <CircularProgress size={21} />
        </div>
      )}
      {isTaskResponseSuccess && (
        <div className={styles.contentContainer}>
          <TaskPageHeader taskTag={taskTag} title={taskResponse.data.title} />
          <TaskDetailBreadcrumb task={taskResponse.data} />
          <TaskTitle title={taskResponse.data.title} />
          <TaskDescription description={taskResponse.data.description} />
          <TaskOwnerInfo
            owner={taskResponse.data.owner}
            createdDate={taskResponse.data.createdDate}
          />
        </div>
      )}
    </div>
  );
};

export default TaskDetailPage;
