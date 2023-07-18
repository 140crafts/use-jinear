import Pagination from "@/components/pagination/Pagination";
import { WorkspaceActivityListResponse } from "@/model/be/jinear-core";
import { CircularProgress } from "@mui/material";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./PaginatedActivityList.module.scss";
import ActivityRow from "./activityRow/ActivityRow";

interface PaginatedActivityListProps {
  id: string;
  name: string;
  response?: WorkspaceActivityListResponse;
  isFetching: boolean;
  isLoading: boolean;
  page: number;
  setPage: React.Dispatch<React.SetStateAction<number>>;
  ignoreGrouping?: boolean;
  listNameClassName?: string;
  hidePaginationOnSinglePages?: boolean;
}

const PaginatedActivityList: React.FC<PaginatedActivityListProps> = ({
  id,
  name,
  response,
  isFetching,
  isLoading,
  page,
  setPage,
  ignoreGrouping,
  listNameClassName,
  hidePaginationOnSinglePages = false,
}) => {
  const { t } = useTranslation();
  const activityList = response?.data.content;
  const emptyOrSinglePage = response?.data?.totalPages == 0 || response?.data?.totalPages == 1;

  return (
    <div id={id} className={cn(styles.container)}>
      <div className={styles.header}>
        <h2 className={listNameClassName}>{name}</h2>
        {!(hidePaginationOnSinglePages && emptyOrSinglePage) && response && (
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
        {activityList?.map((activity, i) => {
          const oneBefore = i == 0 ? null : activityList?.[i - 1];
          const sameGroup = oneBefore && activity.groupId == oneBefore?.groupId;
          return (
            <ActivityRow
              key={`${id}-list-task-${activity.workspaceActivityId}`}
              sameGroup={sameGroup}
              ignoreGrouping={ignoreGrouping}
              activity={activity}
              index={i}
            />
          );
        })}

        {!response?.data.hasContent && (
          <div className={styles.emptyStateContainer}>
            <div className={styles.emptyLabel}>{t("activityListEmpty")}</div>
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

export default PaginatedActivityList;
