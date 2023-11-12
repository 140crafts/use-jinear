import { __DEV__ } from "@/utils/constants";
import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";

export const server = __DEV__ ? "staging.api" : "api";
export const root = __DEV__ ? "http://localhost:8085/" : `https://${server}.jinear.co/`;
// : "http://localhost:8085/";

export const s3Base = "https://storage.googleapis.com/jinear-b0/";

const baseQuery = fetchBaseQuery({
  baseUrl: root,
  prepareHeaders: (headers, { getState }) => {
    return headers;
  },
  credentials: "include",
});

export const tagTypes = [
  "Account-Current",
  "workplace-member-list",
  "workplace-team-list",
  "team-member-list",
  "team-topic-list",
  "workplace-task-with-name-and-tag",
  "team-task-list",
  "team-all-task-list",
  "workflow-status-from-team",
  "retrieve-topic",
  "retrieve-task-activity",
  "team-workflow-task-list",
  "reminder-next-job",
  "workspace-task-list",
  "workspace-invitation-list",
  "account-communication-permissions",
  "count-workspace-notification-events",
  "account-workspace-notification-unread-count",
  "task-checklist",
  "task-subscription",
  "task-subscription-subscriber-list",
  "task-list-with-assignee",
  "task-list-assigned-to-current-account",
  "workspace-activity-list",
  "workspace-team-activity-list",
  "workspace-task-activity-list",
  "task-list-listing",
  "task-list-entry-listing",
  "task-board-entry-listing",
  "retrieve-task-and-task-boards-relation",
  "team-topic-task-list",
  "task-listing-filter",
  "team-topic-search",
  "task-board-listing",
  "team-topic-find-exact",
  "retrieve-topic-by-tag",
  "task-board-filter",
  "task-media-list",
  "task-media-list-from-team",
  "payments-info-workspace-subscription",
  "task-comments",
  "workspace-team-membership-list",
];

export const tagTypesToInvalidateOnNewBackgroundActivity = () => {
  return tagTypes.filter((tag) => tag != "Account-Current");
};

export const api = createApi({
  baseQuery: baseQuery,
  tagTypes,
  endpoints: () => ({}),
});
