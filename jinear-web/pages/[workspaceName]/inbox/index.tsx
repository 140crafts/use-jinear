import InboxScreenHeader from "@/components/inboxScreen/inboxScreenHeader/InboxScreenHeader";
import NotificationList from "@/components/inboxScreen/notificationList/NotificationList";
import { api } from "@/store/api/api";
import { useRetrieveNotificationsQuery } from "@/store/api/notificationEventApi";
import { selectCurrentAccountsPreferredWorkspaceId } from "@/store/slice/accountSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import Logger from "@/utils/logger";
import React, { useEffect, useState } from "react";
import styles from "./index.module.css";

interface InboxScreenProps {}

const logger = Logger("InboxScreen");
const InboxScreen: React.FC<InboxScreenProps> = ({}) => {
  const dispatch = useAppDispatch();
  const workspaceId = useTypedSelector(selectCurrentAccountsPreferredWorkspaceId);
  const [page, setPage] = useState<number>(0);
  const {
    data: response,
    isSuccess,
    isLoading,
    isFetching,
  } = useRetrieveNotificationsQuery({ workspaceId: workspaceId || "", page }, { skip: workspaceId == null });

  useEffect(() => {
    invalidateCountQuery();
  }, [isSuccess]);

  const invalidateCountQuery = () => {
    logger.log("invalidateCountQuery has started");
    dispatch(api.util.invalidateTags(["account-workspace-notification-unread-count"]));
  };

  return (
    <div className={styles.container}>
      <InboxScreenHeader />
      <NotificationList data={response?.data} isFetching={isFetching} isLoading={isLoading} page={page} setPage={setPage} />
    </div>
  );
};

export default InboxScreen;
