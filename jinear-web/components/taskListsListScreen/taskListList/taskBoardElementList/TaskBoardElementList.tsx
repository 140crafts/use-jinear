import PaginatedList from "@/components/paginatedList/PaginatedList";
import { TaskListEntryDto } from "@/model/be/jinear-core";
import { useRetrieveFromTaskListQuery } from "@/store/api/TaskListEntryApi";
import useTranslation from "locales/useTranslation";
import React, { useState } from "react";
import styles from "./TaskBoardElementList.module.css";

interface TaskBoardElementListProps {
  title: string;
  taskListId: string;
}

const TaskBoardElementList: React.FC<TaskBoardElementListProps> = ({ title, taskListId }) => {
  const { t } = useTranslation();
  const [page, setPage] = useState<number>(0);

  const { data: taskBoardElementsResponse, isFetching, isLoading } = useRetrieveFromTaskListQuery({ taskListId });

  const renderItem = (item: TaskListEntryDto) => {
    return <div>{item.task.title}</div>;
  };

  return (
    <div className={styles.container}>
      <PaginatedList
        id={`task-board-element-paginated-${taskListId}`}
        data={taskBoardElementsResponse?.data}
        isFetching={isFetching}
        isLoading={isLoading}
        page={page}
        setPage={setPage}
        renderItem={renderItem}
        emptyLabel={t("taskBoardEmptyLabel")}
        listTitle={title}
        hidePaginationOnSinglePages={true}
      />
    </div>
  );
};

export default TaskBoardElementList;
