import PaginatedList from "@/components/paginatedList/PaginatedList";
import { TaskListDto } from "@/model/be/jinear-core";
import { useRetrieveAllTaskListsQuery } from "@/store/api/TaskListListingApi";
import useTranslation from "locales/useTranslation";
import React, { useState } from "react";
import styles from "./TaskListList.module.css";
import TaskBoardElementList from "./taskBoardElementList/TaskBoardElementList";

interface TaskListListProps {
  teamId: string;
  workspaceId: string;
}

const TaskListList: React.FC<TaskListListProps> = ({ teamId, workspaceId }) => {
  const { t } = useTranslation();
  const [page, setPage] = useState<number>(0);

  const { data: taskListListingResponse, isFetching, isLoading } = useRetrieveAllTaskListsQuery({ teamId, workspaceId });

  const renderItem = (item: TaskListDto) => {
    return <TaskBoardElementList key={item.taskListId} title={item.title} taskListId={item.taskListId} />;
  };

  return (
    <div className={styles.container}>
      <PaginatedList
        id={"task-list-list-paginated"}
        data={taskListListingResponse?.data}
        isFetching={isFetching}
        isLoading={isLoading}
        page={page}
        setPage={setPage}
        renderItem={renderItem}
        emptyLabel={t("taskListsListEmptyLabel")}
        listTitle={t("taskListsListTitle")}
        hidePaginationOnSinglePages={true}
        contentContainerClassName={styles.list}
      />
    </div>
  );
};

export default TaskListList;
