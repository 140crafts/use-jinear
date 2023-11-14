import { WorkspaceActivityListResponse } from "@/model/be/jinear-core";
import { api, tagTypesToInvalidateOnNewBackgroundActivity } from "@/store/api/api";
import { useFilterWorkspaceActivitiesQuery } from "@/store/api/workspaceActivityApi";
import { clearHasUnreadNotificationOnAllTasks } from "@/store/slice/taskAdditionalDataSlice";
import { useAppDispatch } from "@/store/store";
import { __DEV__ } from "@/utils/constants";
import Logger from "@/utils/logger";
import useTranslation from "locales/useTranslation";
import React, { useEffect, useRef } from "react";
import { useSSE } from "react-hooks-sse";
import toast from "react-hot-toast";
import ForegroundNotification from "../foregroundNotification/ForegroundNotification";

interface SseListenerWorkspaceActivitiesProps {
  workspaceId: string;
  workspaceUsername: string;
  children: React.ReactNode;
}

const logger = Logger("SseListenerWorkspaceActivities");
const SseListenerWorkspaceActivities: React.FC<SseListenerWorkspaceActivitiesProps> = ({
  workspaceId,
  workspaceUsername,
  children,
}) => {
  const dispatch = useAppDispatch();
  const { t } = useTranslation();
  const toastId = useRef<string | null>(null);
  const isInitial = useRef<boolean>(true);

  const latestWorkspaceActivityResponseFromSSE = useSSE<WorkspaceActivityListResponse>(
    "workspace-activity",
    {} as WorkspaceActivityListResponse
  );

  const { data: retrieveWorkspaceActivitiesResponse, isFetching: isRetrieveActivitiesQueryFetching } =
    useFilterWorkspaceActivitiesQuery({ workspaceId, page: 0 });

  const latestWorkspaceActivityFromSSE = latestWorkspaceActivityResponseFromSSE?.data?.content?.[0];
  const lastRetrievedActivity = retrieveWorkspaceActivitiesResponse?.data?.content?.[0];

  //   logger.log({ latestWorkspaceActivityResponseFromSSE, lastRetrievedActivity });

  useEffect(() => {
    logger.log({
      latestWorkspaceActivityFromSSE,
      lastRetrievedActivity,
      isRetrieveActivitiesQueryFetching,
      workspaceId,
    });
    if (
      latestWorkspaceActivityFromSSE == null ||
      latestWorkspaceActivityFromSSE?.workspaceId != workspaceId ||
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
      latestWorkspaceActivityFromSSE.workspaceActivityId != lastRetrievedActivity.workspaceActivityId
      //   &&   isAfter(lastRetrievedDate, latestWorkspaceActivityDate)
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
      toast.dismiss(toastId.current || undefined);
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
            duration: __DEV__ ? 8000 : 60000,
          }
        );
        toastId.current = notifToastId;
      }
      isInitial.current = false;
    }
  }, [workspaceId, workspaceUsername, latestWorkspaceActivityFromSSE, lastRetrievedActivity, isRetrieveActivitiesQueryFetching]);

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
    dispatch(api.util.invalidateTags(["workspace-activity-filtered-list"]));
  };

  return <>{children}</>;
};

export default SseListenerWorkspaceActivities;
