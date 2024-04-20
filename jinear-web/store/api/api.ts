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
  credentials: "include"
});

export const tagTypes = [
  "v1/account",
  "v1/workspace/member/{workspaceId}/list",
  "v1/team/from-workspace/{workspaceId}",
  "v1/team/member/list/{teamId}",
  "v1/topic/list/{teamId}",
  "v1/task/from-workspace/{workspaceName}/{taskTag}",
  "v1/team/workflow-status/{teamId}/list",
  "v1/topic/{topicId}",
  "v1/task/reminder/job/{taskReminderId}/next",
  "v1/workspace/member/invitation/list/{workspaceId}",
  "v1/workspace/member/invitation/info/{token}",
  "v1/account/communication-permission",
  "v1/notification/event/{workspaceId}/unread-count",
  "v1/task/checklist",
  "v1/task/{taskId}/subscription",
  "v1/task/{taskId}/subscription/list",
  "v1/task-board/entry/from-task-board/{taskBoardId}",
  "v1/task-board/list/related-with-task/{taskId}",
  "v1/task/list/filter",
  "v1/topic/list/{teamId}/search",
  "v1/task-board/list/${workspaceId}/team/${teamId}",
  "v1/topic/list/{teamId}/retrieve-exact",
  "v1/topic/tag/{topicTag}/workspace/{workspaceId}/team/{teamId}",
  "v1/task-board/list/{workspaceId}/team/{teamId}/filter",
  "v1/task/media/{taskId}",
  "v1/task/media/from-team/{teamId}",
  "v1/payments/info/workspace/{workspaceId}/subscription",
  "v1/task/comment/from-task/{taskId}",
  "v1/team/member/memberships/{workspaceId}",
  "v1/workspace/activity/filter",
  "v1/oauth/google/redirect-info/login",
  "v1/feed/content/workspace/{workspaceId}/{feedId}",
  "v1/feed/content/workspace/{workspaceId}/{feedId}/item/{itemId}",
  "v1/team/{teamId}/team-state/{teamState}",
  "v1/oauth/google/redirect-info/attach-mail",
  "v1/oauth/google/redirect-info/attach-calendar",
  "v1/feed/member/memberships/{workspaceId}",
  "v1/task/feed-item/from-task/req.taskId",
  "v1/feed/member/list/{feedId}",
  "v1/task-analytics/{workspaceId}/team/{teamId}",
  "v1/notification/event/{workspaceId}",
  "v1/notification/event/{workspaceId}/team/{teamId}",
  "v1/task/search/${workspaceId}/${teamId}/{title}",
  "v1/calendar/member/memberships/{workspaceId}",
  "v1/calendar/member/list/{calendarId}",
  "v1/calendar/event/filter",
  "v1/calendar/event/exports/workspace/{workspaceId}/key",
  "v1/account/delete",
  "v1/account/delete/eligibility",
  "v1/account/delete/confirm/{token}",
  "v1/messaging/channel/member/memberships/{workspaceId}",
  "v1/messaging/channel/member/list/{channelId}",
  "v1/messaging/thread/channel/{channelId}",
  "v1/messaging/message/thread/{threadId}"
];

export const tagTypesToInvalidateOnNewBackgroundActivity = () => {
  return tagTypes.filter((tag) => tag != "v1/account");
};

export const api = createApi({
  baseQuery: baseQuery,
  tagTypes,
  endpoints: () => ({})
});
