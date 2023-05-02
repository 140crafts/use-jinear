import Pagination from "@/components/pagination/Pagination";

import { NotificationEventDto, PageDto } from "@/model/be/jinear-core";
import { CircularProgress } from "@mui/material";
import cn from "classnames";
import { isSameDay } from "date-fns";
import useTranslation from "locales/useTranslation";
import React from "react";
import styles from "./NotificationList.module.scss";
import NotificationMessageRow from "./notificationMessageRow/NotificationMessageRow";

interface NotificationListProps {
  data?: PageDto<NotificationEventDto>;
  isFetching: boolean;
  isLoading: boolean;
  page: number;
  setPage: React.Dispatch<React.SetStateAction<number>>;
}

const NotificationList: React.FC<NotificationListProps> = ({ data, isFetching, isLoading, page, setPage }) => {
  const { t } = useTranslation();
  return (
    <div className={styles.container}>
      <div className={styles.header}>
        <h2>{t("notificationEventListName")}</h2>
        {data && (
          <Pagination
            id={`notification-events-paginator`}
            className={styles.pagination}
            pageNumber={data?.number}
            pageSize={data?.size}
            totalPages={data?.totalPages}
            totalElements={data?.totalElements}
            hasPrevious={data?.hasPrevious}
            hasNext={data?.hasNext}
            isLoading={isLoading || isFetching}
            page={page}
            setPage={setPage}
          />
        )}
      </div>

      <div className={cn(styles.content, styles.gradientBg)}>
        {data?.content.map((notificationEvent, i) => {
          const oneBefore = i == 0 ? null : data.content?.[i - 1];
          const datesEqual = oneBefore && isSameDay(new Date(oneBefore.createdDate), new Date(notificationEvent.createdDate));
          return (
            <NotificationMessageRow
              key={`${notificationEvent.notificationEventId}`}
              notificationEvent={notificationEvent}
              withDateTitle={!datesEqual}
            />
          );
        })}

        {!data?.hasContent && (
          <div className={styles.emptyStateContainer}>
            <div className={styles.emptyLabel}>{t("notificationEventListEmpty")}</div>
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

export default NotificationList;
