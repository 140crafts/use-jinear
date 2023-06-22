import InboxScreenHeader from "@/components/inboxScreen/inboxScreenHeader/InboxScreenHeader";
import NotificationList from "@/components/inboxScreen/notificationList/NotificationList";
import { TeamDto } from "@/model/be/jinear-core";
import { api } from "@/store/api/api";
import { useRetrieveNotificationsQuery, useRetrieveTeamNotificationsQuery } from "@/store/api/notificationEventApi";
import { selectWorkspaceFromWorkspaceUsername } from "@/store/slice/accountSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import Logger from "@/utils/logger";
import { useRouter } from "next/router";
import React, { useEffect, useState } from "react";
import styles from "./index.module.css";

interface InboxScreenProps {}

const logger = Logger("InboxScreen");
const InboxScreen: React.FC<InboxScreenProps> = ({}) => {
  const router = useRouter();
  const dispatch = useAppDispatch();
  const workspaceName: string = router.query?.workspaceName as string;
  const workspace = useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));

  const [filterBy, setFilterBy] = useState<TeamDto>();

  const [page, setPage] = useState<number>(0);

  const {
    data: workspaceNotificationsResponse,
    isSuccess: isWorkspaceNotificationsSuccess,
    isLoading: isWorkspaceNotificationsLoading,
    isFetching: isWorkspaceNotificationsFetching,
  } = useRetrieveNotificationsQuery({ workspaceId: workspace?.workspaceId || "", page }, { skip: workspace == null });

  const {
    data: teamNotificationsResponse,
    isSuccess: isTeamNotificationsSuccess,
    isLoading: isTeamNotificationsLoading,
    isFetching: isTeamNotificationsFetching,
  } = useRetrieveTeamNotificationsQuery(
    { workspaceId: workspace?.workspaceId || "", teamId: filterBy?.teamId || "", page },
    { skip: workspace == null || filterBy == null }
  );

  const isSuccess = filterBy ? isTeamNotificationsSuccess : isWorkspaceNotificationsSuccess;
  const isLoading = filterBy ? isTeamNotificationsLoading : isWorkspaceNotificationsLoading;
  const isFetching = filterBy ? isTeamNotificationsFetching : isWorkspaceNotificationsFetching;
  const response = filterBy ? teamNotificationsResponse : workspaceNotificationsResponse;

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
