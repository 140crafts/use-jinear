import TaskDetail from "@/components/taskDetail/TaskDetail";
import TaskDetailHeader from "@/components/taskDetail/taskDetailHeader/TaskDetailHeader";
import TaskPageHeader from "@/components/taskDetail/taskPageHeader/TaskPageHeader";
import { useRetrieveWithWorkspaceNameAndTeamTagNoQuery } from "@/store/api/taskApi";
import { useUpdatePreferredTeamMutation, useUpdatePreferredWorkspaceMutation } from "@/store/api/workspaceDisplayPreferenceApi";
import { selectCurrentAccountsPreferredTeamId, selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { changeLoadingModalVisibility } from "@/store/slice/modalSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import Logger from "@/utils/logger";
import { CircularProgress } from "@mui/material";
import { useRouter } from "next/router";
import React, { useEffect } from "react";
import styles from "./index.module.scss";

interface TaskDetailPageProps {}
const logger = Logger("TaskDetailPage");

const TaskDetailPage: React.FC<TaskDetailPageProps> = ({}) => {
  const dispatch = useAppDispatch();
  const router = useRouter();
  const currentWorkspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);
  const preferredTeamId = useTypedSelector(selectCurrentAccountsPreferredTeamId);
  const workspaceName: string = router.query?.workspaceName as string;
  const taskTag: string = router.query?.taskTag as string;

  const {
    data: taskResponse,
    isLoading: isTaskResponseLoading,
    isSuccess: isTaskResponseSuccess,
    isFetching: isTaskRetrieveFetching,
  } = useRetrieveWithWorkspaceNameAndTeamTagNoQuery(
    { workspaceName, taskTag },
    { skip: workspaceName == null || taskTag == null }
  );

  const [updatePreferredWorkspace] = useUpdatePreferredWorkspaceMutation();
  const [updatePreferredTeam] = useUpdatePreferredTeamMutation();

  useEffect(() => {
    dispatch(changeLoadingModalVisibility({ visible: isTaskRetrieveFetching }));
  }, [isTaskRetrieveFetching]);

  useEffect(() => {
    if (currentWorkspace && isTaskResponseSuccess && taskResponse && preferredTeamId) {
      if (taskResponse.data.workspaceId != currentWorkspace.workspaceId) {
        logger.log({
          taskResponseWorkspaceId: taskResponse.data.workspaceId,
          currentWorkspaceWorkspaceId: currentWorkspace.workspaceId,
        });
        updatePreferredWorkspace({
          workspaceId: taskResponse.data.workspaceId,
          dontReroute: true,
        });
      }
      if (preferredTeamId != taskResponse.data.teamId) {
        logger.log({
          preferredTeamId,
          taskResponseTeamId: taskResponse.data.teamId,
        });
        updatePreferredTeam({
          teamId: taskResponse.data.teamId,
          workspaceId: taskResponse.data.workspaceId,
          dontReroute: true,
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
        <>
          <TaskPageHeader taskTag={taskTag} title={taskResponse.data.title} />
          <div className={styles.contentContainer}>
            <TaskDetailHeader task={taskResponse.data} backButtonVisible={true} />
            <TaskDetail task={taskResponse.data} />
          </div>
        </>
      )}
    </div>
  );
};

export default TaskDetailPage;
