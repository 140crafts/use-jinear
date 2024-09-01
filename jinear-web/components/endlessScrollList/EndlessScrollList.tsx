import { CircularProgress } from "@mui/material";
import cn from "classnames";
import { ReactElement } from "react";
import Button, { ButtonVariants } from "../button";
import styles from "./EndlessScrollList.module.scss";

interface EndlessScrollListProps<T> {
  id: string;
  listTitle?: string;
  data: T[];
  isFetching: boolean;
  renderItem: (data: T, index: number) => ReactElement | null;
  emptyLabel: string;
  hasMore: boolean;
  loadMore?: () => void;
  loadMoreLabel?: string;
  loadMoreLoading?: boolean;
  containerClassName?: string;
  contentContainerClassName?: string;
  listTitleComponent?: ReactElement;
  listTitleClassName?: string;
}

const EndlessScrollList = <T,>({
  id,
  listTitle,
  data,
  isFetching,
  renderItem,
  emptyLabel,
  hasMore,
  loadMore,
  loadMoreLabel,
  loadMoreLoading,
  containerClassName,
  contentContainerClassName,
  listTitleComponent,
  listTitleClassName,
}: EndlessScrollListProps<T>): ReactElement | null => {
  return (
    <div id={id} className={cn(styles.container, containerClassName)}>
      <div className={styles.header}>
        {listTitleComponent ? listTitleComponent : <h2 className={listTitleClassName}>{listTitle}</h2>}
      </div>
      <div className={cn(styles.content, styles.gradientBg, contentContainerClassName)}>
        {data?.map(renderItem)}

        {data?.length == 0 && emptyLabel && (
          <div className={styles.emptyStateContainer}>
            <div className={styles.emptyLabel}>{emptyLabel}</div>
          </div>
        )}
      </div>

      {isFetching && (
        <div className={styles.loading}>
          <CircularProgress size={12} />
        </div>
      )}

      {hasMore && (
        <Button loading={loadMoreLoading} disabled={loadMoreLoading} variant={ButtonVariants.filled} onClick={loadMore}>
          {loadMoreLabel}
        </Button>
      )}
    </div>
  );
};

export default EndlessScrollList;
