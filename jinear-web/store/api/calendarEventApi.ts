import {
  BaseResponse,
  CalendarEventDateUpdateRequest,
  CalendarEventFilterRequest,
  CalendarEventListingResponse,
  CalendarEventTitleDescriptionUpdateRequest,
  CalendarShareableKeyResponse,
} from "@/model/be/jinear-core";
import { api } from "./api";

export const calendarEventApi = api.injectEndpoints({
  endpoints: (build) => ({
    //
    filterCalendarEvents: build.query<CalendarEventListingResponse, CalendarEventFilterRequest>({
      query: (req) => ({ url: `v1/calendar/event/filter`, method: "POST", body: req }),
      providesTags: (_result, _err, req) => [
        {
          type: "v1/calendar/event/filter",
          id: `${JSON.stringify(req)}`,
        },
      ],
    }),
    //
    updateCalendarEventDates: build.mutation<BaseResponse, CalendarEventDateUpdateRequest>({
      query: (req) => ({ url: `v1/calendar/event/update/from-external/dates`, method: "PUT", body: req }),
      invalidatesTags: ["v1/calendar/event/filter", "v1/task/list/filter"],
    }),
    //
    updateTitleAndDescription: build.mutation<BaseResponse, CalendarEventTitleDescriptionUpdateRequest>({
      query: (req) => ({ url: `v1/calendar/event/update/from-external/title-description`, method: "PUT", body: req }),
      invalidatesTags: ["v1/calendar/event/filter", "v1/task/list/filter"],
    }),
    //
    retrieveShareableKey: build.query<CalendarShareableKeyResponse, { workspaceId: string }>({
      query: ({ workspaceId }: { workspaceId: string }) => `v1/calendar/event/exports/workspace/${workspaceId}/key`,
      providesTags: (_result, _err, req) => [
        {
          type: `v1/calendar/event/exports/workspace/{workspaceId}/key`,
          id: req.workspaceId,
        },
      ],
    }),
    //
    refreshShareableKey: build.mutation<BaseResponse, { workspaceId: string }>({
      query: ({ workspaceId }: { workspaceId: string }) => ({
        url: `v1/calendar/event/exports/workspace/${workspaceId}/key/refresh`,
        method: "POST",
      }),
      invalidatesTags: [`v1/calendar/event/exports/workspace/{workspaceId}/key`],
    }),
    //
  }),
});

export const {
  useFilterCalendarEventsQuery,
  useUpdateCalendarEventDatesMutation,
  useUpdateTitleAndDescriptionMutation,
  useRetrieveShareableKeyQuery,
  useRefreshShareableKeyMutation,
} = calendarEventApi;

export const {
  endpoints: {
    filterCalendarEvents,
    updateCalendarEventDates,
    updateTitleAndDescription,
    retrieveShareableKey,
    refreshShareableKey,
  },
} = calendarEventApi;
