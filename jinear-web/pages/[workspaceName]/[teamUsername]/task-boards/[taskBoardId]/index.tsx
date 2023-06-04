import TaskBoardDetailBreadcrumb from "@/components/taskBoardDetailScreen/taskBoardDetailBreadcrumb/TaskBoardDetailBreadcrumb";
import TaskBoardElementList from "@/components/taskLists/taskBoardList/taskBoardElementList/TaskBoardElementList";
import { useRetrieveTaskBoardQuery } from "@/store/api/taskBoardRetrieveApi";
import { selectCurrentAccountsPreferredTeam, selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";
import { CircularProgress } from "@mui/material";
import { useRouter } from "next/router";
import React from "react";
import styles from "./index.module.css";

interface TaskBoardDetailScreenProps {}

const TaskBoardDetailScreen: React.FC<TaskBoardDetailScreenProps> = ({}) => {
  const router = useRouter();
  const taskBoardId: string = router.query?.taskBoardId as string;

  const team = useTypedSelector(selectCurrentAccountsPreferredTeam);
  const workspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);

  const {
    data: taskBoardResponse,
    isLoading,
    isFetching,
  } = useRetrieveTaskBoardQuery({ taskBoardId }, { skip: taskBoardId == null });

  return (
    <div className={styles.container}>
      <TaskBoardDetailBreadcrumb title={taskBoardResponse?.data.title || ""} taskBoardId={taskBoardId} />
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
