import { WorkspaceActivityResponse } from "@/model/be/jinear-core";
import { api, root, tagTypesToInvalidateOnNewBackgroundActivity } from "@/store/api/api";
import { useFilterWorkspaceActivitiesQuery } from "@/store/api/workspaceActivityApi";
import { selectCurrentAccountId, selectCurrentAccountsPreferredWorkspace } from "@/store/slice/accountSlice";
import { selectLatestWorkspaceActivity, setLatestWorkspaceActivity } from "@/store/slice/sseSlice";
import { clearHasUnreadNotificationOnAllTasks } from "@/store/slice/taskAdditionalDataSlice";
import { useAppDispatch, useTypedSelector } from "@/store/store";
import Logger from "@/utils/logger";
import { isAfter } from "date-fns";
import useTranslation from "locales/useTranslation";
import React, { useEffect, useRef, useState } from "react";
import { toast } from "react-hot-toast";
import ForegroundNotification from "../foregroundNotification/ForegroundNotification";

interface SseListenerProps {}

const logger = Logger("SseListener");
const SseListener: React.FC<SseListenerProps> = ({}) => {
  const { t } = useTranslation();
  const dispatch = useAppDispatch();
  const accountId = useTypedSelector(selectCurrentAccountId);
  const workspace = useTypedSelector(selectCurrentAccountsPreferredWorkspace);
  const workspaceId = workspace?.workspaceId;
  const workspaceUsername = workspace?.username;
  const [sseWorkspaceActivity, setSseWorkspaceActivity] = useState<EventSource>();
  const toastId = useRef<string | null>(null);
  const isInitial = useRef<boolean>(true);

  const latestWorkspaceActivityFromSSE = useTypedSelector(selectLatestWorkspaceActivity);

  // const { data: retrieveWorkspaceActivitiesResponse, isFetching: isRetrieveActivitiesQueryFetching } = useRetrieveActivitiesQuery(
  //   { workspaceId: workspaceId || "", page: 0 },
  //   { skip: workspaceId == null }
  // );
  const { data: retrieveWorkspaceActivitiesResponse, isFetching: isRetrieveActivitiesQueryFetching } =
    useFilterWorkspaceActivitiesQuery({ workspaceId: workspaceId || "", page: 0 });

  const lastRetrievedActivity = retrieveWorkspaceActivitiesResponse?.data?.content?.[0];

  useEffect(() => {
    if (sseWorkspaceActivity) {
      sseWorkspaceActivity.removeEventListener("workspace-activity", onMessage);
      sseWorkspaceActivity.close();
      setSseWorkspaceActivity(undefined);
      dispatch(setLatestWorkspaceActivity(undefined));
      logger.log("Sse uninitialized.");
    }
    if (accountId && workspaceId) {
      const url = `${root}v1/sse/workspace/activity/${workspaceId}`;
      const sse = new EventSource(url, { withCredentials: true });
      sse.addEventListener("workspace-activity", onMessage);
      setSseWorkspaceActivity(sse);
      logger.log({ sseInitializedForUrl: url });
    }
  }, [accountId, workspaceId]);

  useEffect(() => {
    if (
      latestWorkspaceActivityFromSSE == null ||
      latestWorkspaceActivityFromSSE?.workspaceId != workspaceId ||
      retrieveWorkspaceActivitiesResponse == null ||
      lastRetrievedActivity == null ||
      isRetrieveActivitiesQueryFetching
    ) {
      return;
    }
    const lastRetrievedDate =
      lastRetrievedActivity?.createdDate != null ? new Date(lastRetrievedActivity?.createdDate) : new Date();
    const latestWorkspaceActivityDate =
      latestWorkspaceActivityFromSSE?.createdDate != null ? new Date(latestWorkspaceActivityFromSSE?.createdDate) : new Date();

    if (
      latestWorkspaceActivityFromSSE.workspaceActivityId != lastRetrievedActivity.workspaceActivityId &&
      isAfter(lastRetrievedDate, latestWorkspaceActivityDate)
    ) {
      logger.log({
        on: "Diff",
        latestWorkspaceActivityFromSSE,
        lastRetrievedActivity,
        toastId: toastId.current,
        isInitial: isInitial.current,
      });
      const title = t("newChangesExistsToastTitle");
      const body = t("newChangesExistsToastBody");
      if (toastId.current == null || isInitial.current == false) {
        const notifToastId = toast(
          (t) => (
            <ForegroundNotification
              title={title}
              body={body}
              launchUrl={`/${workspaceUsername}/last-activities`}
              onClick={invalidateEverything}
              onClose={resetToastState}
              closeable={true}
            />
          ),
          {
            position: window.innerWidth < 768 ? "top-center" : "top-right",
            duration: 60000000,
          }
        );
        toastId.current = notifToastId;
      }
      isInitial.current = false;
    }
  }, [workspaceId, latestWorkspaceActivityFromSSE, lastRetrievedActivity, isRetrieveActivitiesQueryFetching]);

  useEffect(() => {
    if (isRetrieveActivitiesQueryFetching && toastId.current) {
      toast.dismiss(toastId.current);
      toastId.current = null;
    }
  }, [isRetrieveActivitiesQueryFetching, toastId]);

  const invalidateEverything = () => {
    dispatch(api.util.invalidateTags(tagTypesToInvalidateOnNewBackgroundActivity()));
    dispatch(clearHasUnreadNotificationOnAllTasks());
    setTimeout(() => {
      toast.dismiss(toastId.current || undefined);
      toastId.current = null;
    }, 2500);
  };

  const resetToastState = () => {
    toast.dismiss(toastId.current || undefined);
    toastId.current = null;
    dispatch(api.util.invalidateTags(["workspace-activity-list"]));
  };

  const onMessage = (e: MessageEvent<any>) => {
    try {
      const rawData = e.data;
      const workspaceActivityResponse: WorkspaceActivityResponse = JSON.parse(rawData);
      const latestActivity = workspaceActivityResponse.data;
      dispatch(setLatestWorkspaceActivity(latestActivity));
    } catch (e) {
      console.error(e);
    }
  };

  return null;
};

export default SseListener;
