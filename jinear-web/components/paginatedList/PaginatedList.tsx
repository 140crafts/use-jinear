import { PageDto } from "@/model/be/jinear-core";
import { CircularProgress } from "@mui/material";
import cn from "classnames";
import useTranslation from "locales/useTranslation";
import React, { ReactElement } from "react";
import Pagination from "../pagination/Pagination";
import styles from "./PaginatedList.module.scss";

interface PaginatedListProps<T> {
  id: string;
  listTitle?: string;
  data?: PageDto<T>;
  isFetching: boolean;
  isLoading: boolean;
  page: number;
  setPage: React.Dispatch<React.SetStateAction<number>>;
  renderItem: (data: T, index: number) => ReactElement;
  emptyLabel: string;
  hidePaginationOnSinglePages?: boolean;
  containerClassName?: string;
  contentContainerClassName?: string;
  listTitleComponent?: ReactElement;
}

const PaginatedList = <T,>({
  id,
  listTitle,
  data,
  isFetching,
  isLoading,
  page,
  setPage,
  renderItem,
  emptyLabel,
  hidePaginationOnSinglePages = false,
  containerClassName,
  contentContainerClassName,
  listTitleComponent,
}: PaginatedListProps<T>): ReactElement | null => {
  const { t } = useTranslation();

  const emptyOrSinglePage = data?.totalPages == 0 || data?.totalPages == 1;

  return (
    <div id={id} className={cn(styles.container, containerClassName)}>
      <div className={styles.header}>
        {listTitleComponent ? listTitleComponent : <h2>{listTitle}</h2>}
        {!(hidePaginationOnSinglePages && emptyOrSinglePage) && data && (
          <Pagination
            id={`${id}-paginator`}
            className={styles.pagination}
            pageNumber={data.number}
            pageSize={data.size}
            totalPages={data.totalPages}
            totalElements={data.totalElements}
            hasPrevious={data.hasPrevious}
            hasNext={data.hasNext}
            isLoading={isLoading || isFetching}
            page={page}
            setPage={setPage}
          />
        )}
      </div>

      <div className={cn(styles.content, styles.gradientBg, contentContainerClassName)}>
        {data?.content.map(renderItem)}

        {!data?.hasContent && (
          <div className={styles.emptyStateContainer}>
            <div className={styles.emptyLabel}>{emptyLabel}</div>
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

export default PaginatedList;
