import Pagination from "@/components/pagination/Pagination";
import { useRetrieveFromWorkflowStatusQuery } from "@/store/api/taskListingApi";
import React, { useState } from "react";
import TaskRow from "./taskRow/TaskRow";
import styles from "./WorkflowTaskList.module.scss";

interface WorkflowTaskListProps {
  workspaceId: string;
  teamId: string;
  workflowStatusId: string;
  name: string;
}

const WorkflowTaskList: React.FC<WorkflowTaskListProps> = ({
  workspaceId,
  teamId,
  workflowStatusId,
  name,
}) => {
  const [page, setPage] = useState<number>(0);
  const { data: workflowTaskListResponse, isLoading } =
    useRetrieveFromWorkflowStatusQuery({
      workspaceId,
      teamId,
      workflowStatusId,
      page,
    });

  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <h2>{name}</h2>
        {workflowTaskListResponse && (
          <Pagination
            id={workflowStatusId}
            className={styles.pagination}
            pageNumber={workflowTaskListResponse.data.number}
            pageSize={workflowTaskListResponse.data.size}
            totalPages={workflowTaskListResponse.data.totalPages}
            totalElements={workflowTaskListResponse.data.totalElements}
            hasPrevious={workflowTaskListResponse.data.hasPrevious}
            hasNext={workflowTaskListResponse.data.hasNext}
            isLoading={isLoading}
            page={page}
            setPage={setPage}
          />
        )}
      </div>

      <div className={styles.content}>
        {workflowTaskListResponse?.data.content.map((taskDto) => (
          <TaskRow key={`task-list-${taskDto.taskId}`} task={taskDto} />
        ))}
      </div>
    </div>
  );
};

export default WorkflowTaskList;
