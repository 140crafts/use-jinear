"use client";
import TaskDetail from "@/components/taskDetail/TaskDetail";
import TaskPageHeader from "@/components/taskDetail/taskPageHeader/TaskPageHeader";
import { useRetrieveWithWorkspaceNameAndTeamTagNoQuery } from "@/store/api/taskApi";
import { changeLoadingModalVisibility } from "@/store/slice/modalSlice";
import { useAppDispatch } from "@/store/store";
import Logger from "@/utils/logger";
import { CircularProgress } from "@mui/material";
import { useParams } from "next/navigation";
import React, { useEffect } from "react";
import styles from "./index.module.scss";

interface TaskDetailPageProps {}
const logger = Logger("TaskDetailPage");

const TaskDetailPage: React.FC<TaskDetailPageProps> = ({}) => {
  const dispatch = useAppDispatch();
  const params = useParams();

  const workspaceName: string = params?.workspaceName as string;
  const taskTag: string = params?.taskTag as string;

  const {
    data: taskResponse,
    isLoading: isTaskResponseLoading,
    isSuccess: isTaskResponseSuccess,
    isFetching: isTaskRetrieveFetching,
  } = useRetrieveWithWorkspaceNameAndTeamTagNoQuery(
    { workspaceName, taskTag },
    { skip: workspaceName == null || taskTag == null }
  );

  useEffect(() => {
    dispatch(changeLoadingModalVisibility({ visible: isTaskRetrieveFetching }));
  }, [isTaskRetrieveFetching]);

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
            {/* <TaskDetailHeader task={taskResponse.data} backButtonVisible={true} /> */}
            <TaskDetail task={taskResponse.data} />
          </div>
        </>
      )}
    </div>
  );
};

export default TaskDetailPage;
