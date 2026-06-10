import React, { useState } from "react";
import styles from "./TaskBoardColumnView.module.css";
import TeamWorkflowStatusBoard from "@/components/teamWorkflowStatusBoard/TeamWorkflowStatusBoard";
import { useRetrieveFromTaskBoardQuery } from "@/api/taskBoardEntryApi";
import cn from "classnames";

interface TaskBoardColumnViewProps {
  taskBoardId: string;
  teamId: string;
  className?: string;
}

const TaskBoardColumnView: React.FC<TaskBoardColumnViewProps> = ({
                                                                   taskBoardId,
                                                                   teamId,
                                                                   className
                                                                 }) => {
  const [page, setPage] = useState<number>(0);
  const { data: taskBoardElementsResponse, isFetching, isLoading } = useRetrieveFromTaskBoardQuery({
    taskBoardId,
    page
  });

  return (
    <div className={cn(styles.statusBoardContainer, className)}>
      <TeamWorkflowStatusBoard
        teamId={teamId}
        taskList={taskBoardElementsResponse?.data?.content?.map(a => a.task) || []}
        isTaskListingLoading={isFetching}
        className={styles.workflowStatusBoard}
      />
    </div>
  );
};

export default TaskBoardColumnView;