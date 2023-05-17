import { NotificationEventListingResponse, RetrieveUnreadNotificationEventCountResponse } from "@/model/be/jinear-core";
import { api } from "./api";

export const notificationEventApi = api.injectEndpoints({
  endpoints: (build) => ({
    retrieveNotifications: build.query<
      NotificationEventListingResponse,
      {
        workspaceId: string;
        page: number;
      }
    >({
      query: (req: { workspaceId: string; page: number }) => {
        const page = req.page ? req.page : 0;
        return `v1/notification/event/${req.workspaceId}?page=${page}`;
      },
      providesTags: (_result, _err, req) => [
        {
          type: "account-workspace-notification-events",
          id: `${req.workspaceId}-${req.page}`,
        },
      ],
    }),
    //
    retrieveTeamNotifications: build.query<
      NotificationEventListingResponse,
      {
        workspaceId: string;
        teamId: string;
        page: number;
      }
    >({
      query: (req: { workspaceId: string; teamId: string; page: number }) => {
        const page = req.page ? req.page : 0;
        return `v1/notification/event/${req.workspaceId}/team/${req.teamId}?page=${page}`;
      },
      providesTags: (_result, _err, req) => [
        {
          type: "account-workspace-team-notification-events",
          id: `${req.workspaceId}-${req.teamId}-${req.page}`,
        },
      ],
    }),
    //
    retrieveUnreadNotificationCount: build.query<
      RetrieveUnreadNotificationEventCountResponse,
      {
        workspaceId: string;
      }
    >({
      query: (req: { workspaceId: string }) => `v1/notification/event/${req.workspaceId}/unread-count`,
      providesTags: (_result, _err, req) => [
        {
          type: "account-workspace-notification-unread-count",
          id: `${req.workspaceId}`,
        },
      ],
    }),
    //
  }),
});

export const { useRetrieveNotificationsQuery, useRetrieveTeamNotificationsQuery, useRetrieveUnreadNotificationCountQuery } =
  notificationEventApi;

export const {
  endpoints: { retrieveNotifications, retrieveUnreadNotificationCount },
} = notificationEventApi;
