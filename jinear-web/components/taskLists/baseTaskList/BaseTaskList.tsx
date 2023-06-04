import Pagination from "@/components/pagination/Pagination";
import { TaskListingPaginatedResponse } from "@/model/be/jinear-core";
import { CircularProgress } from "@mui/material";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React from "react";
import TaskRow from "../taskRow/TaskRow";
import styles from "./BaseTaskList.module.scss";

interface BaseTaskListProps {
  id: string;
  name: string;
  response?: TaskListingPaginatedResponse;
  isFetching: boolean;
  isLoading: boolean;
  page: number;
  setPage: React.Dispatch<React.SetStateAction<number>>;
}

const BaseTaskList: React.FC<BaseTaskListProps> = ({ id, name, response, isFetching, isLoading, page, setPage }) => {
  const { t } = useTranslation();
  return (
    <div id={id} className={cn(styles.container)}>
      <div className={styles.header}>
        <h2>{name}</h2>
        {response && (
          <Pagination
            id={`${id}-paginator`}
            className={styles.pagination}
            pageNumber={response.data.number}
            pageSize={response.data.size}
            totalPages={response.data.totalPages}
            totalElements={response.data.totalElements}
            hasPrevious={response.data.hasPrevious}
            hasNext={response.data.hasNext}
            isLoading={isLoading || isFetching}
            page={page}
            setPage={setPage}
          />
        )}
      </div>

      <div className={cn(styles.content, styles.gradientBg)}>
        {response?.data.content.map((taskDto) => (
          <TaskRow key={`${id}-list-task-${taskDto.taskId}`} task={taskDto} />
        ))}

        {!response?.data.hasContent && (
          <div className={styles.emptyStateContainer}>
            <div className={styles.emptyLabel}>{t("workflowTaskListEmpty")}</div>
          </div>
        )}
      </div>

      {isFetching && (
        <div className={styles.loading}>
          <CircularProgress />
        </div>
      )}
    </div>
  );
};

export default BaseTaskList;
