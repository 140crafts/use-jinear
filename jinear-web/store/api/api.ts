import { __DEV__ } from "@/utils/constants";
import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";

export const server = __DEV__ ? "staging.jinear" : "api.jinear";
export const root = __DEV__ ? "http://localhost:8085/" : `https://${server}.140crafts.com/`;
// : "http://localhost:8085/";

export const s3Base = "https://storage.googleapis.com/jienar-b0/";

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
  "workflow-status-from-team",
  "retrieve-topic",
  "retrieve-task-activity",
  "team-workflow-task-list",
  "reminder-next-job",
  "workspace-task-list",
  "workspace-invitation-list",
];

export const api = createApi({
  baseQuery: baseQuery,
  tagTypes,
  endpoints: () => ({}),
});
