import InboxScreenHeader from "@/components/inboxScreen/inboxScreenHeader/InboxScreenHeader";
import NotificationList from "@/components/inboxScreen/notificationList/NotificationList";
import { TeamDto } from "@/model/be/jinear-core";
import { api } from "@/store/api/api";
import { useRetrieveNotificationsQuery, useRetrieveTeamNotificationsQuery } from "@/store/api/notificationEventApi";
import { selectCurrentAccountsPreferredTeam, selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import Logger from "@/utils/logger";
import React, { useEffect, useState } from "react";
import styles from "./index.module.css";

interface InboxScreenProps {}

const logger = Logger("InboxScreen");
const InboxScreen: React.FC<InboxScreenProps> = ({}) => {
  const dispatch = useAppDispatch();
  const [filterBy, setFilterBy] = useState<TeamDto>();
  const workspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);
  const team = useTypedSelector(selectCurrentAccountsPreferredTeam);
  const query = filterBy && team ? useRetrieveTeamNotificationsQuery : useRetrieveNotificationsQuery;
  const [page, setPage] = useState<number>(0);

  const {
    data: response,
    isSuccess,
    isLoading,
    isFetching,
  } = query({ workspaceId: workspace?.workspaceId || "", teamId: team?.teamId || "", page }, { skip: workspace == null });

  useEffect(() => {
    invalidateCountQuery();
  }, [isSuccess]);

  const invalidateCountQuery = () => {
    logger.log("invalidateCountQuery has started");
    dispatch(api.util.invalidateTags(["account-workspace-notification-unread-count"]));
  };

  return (
    <div className={styles.container}>
      <InboxScreenHeader filterBy={filterBy} setFilterBy={setFilterBy} workspace={workspace} />
      <NotificationList data={response?.data} isFetching={isFetching} isLoading={isLoading} page={page} setPage={setPage} />
    </div>
  );
};

export default InboxScreen;
