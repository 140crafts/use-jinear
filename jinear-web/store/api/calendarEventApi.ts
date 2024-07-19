import {
  BaseResponse,
  CalendarEventDateUpdateRequest,
  CalendarEventFilterRequest,
  CalendarEventInitializeRequest,
  CalendarEventListingResponse, CalendarEventMoveRequest,
  CalendarEventTitleDescriptionUpdateRequest,
  CalendarShareableKeyResponse
} from "@/model/be/jinear-core";
import { api } from "./api";
import { taskListingApi } from "./taskListingApi";

export const calendarEventApi = api.injectEndpoints({
  endpoints: (build) => ({
    //
    filterCalendarEvents: build.query<CalendarEventListingResponse, CalendarEventFilterRequest>({
      query: (req) => ({ url: `v1/calendar/event/filter`, method: "POST", body: req }),
      providesTags: (_result, _err, req) => [
        {
          type: "v1/calendar/event/filter",
          id: `${JSON.stringify(req)}`
        }
      ]
    }),
    //
    insertEvent: build.mutation<BaseResponse, CalendarEventInitializeRequest>({
      query: (req) => ({ url: `v1/calendar/event/update/from-external`, method: "POST", body: req }),
      invalidatesTags: ["v1/calendar/event/filter", "v1/task/list/filter"]
    }),
    //
    moveEvent: build.mutation<BaseResponse, CalendarEventMoveRequest>({
      query: (req) => ({ url: `v1/calendar/event/update/from-external/move`, method: "POST", body: req }),
      onQueryStarted(req, { dispatch, queryFulfilled, getState }) {
        //cgds-448 might need later
        const invalidatedTaskListFilterCaches = taskListingApi.util.selectInvalidatedBy(getState(), ["v1/task/list/filter"]);
        const invalidatedEventFilterCaches = calendarEventApi.util.selectInvalidatedBy(getState(), ["v1/calendar/event/filter"]);
        invalidatedEventFilterCaches
          .filter((cache) => cache.endpointName == "filterCalendarEvents")
          .forEach((cache) => {
            dispatch(
              calendarEventApi.util.updateQueryData("filterCalendarEvents", cache.originalArgs, (draft) => {
                draft.data
                  .filter((calendarEventDto) => calendarEventDto.calendarEventSourceType != "TASK")
                  .filter(
                    (calendarEventDto) =>
                      calendarEventDto.calendarEventId == req.eventId && calendarEventDto.calendarId == req.calendarId
                  )
                  .forEach((calendarEventDto) => {
                    if (req.targetCalendarSourceId && calendarEventDto.externalCalendarSourceDto) {
                      calendarEventDto.externalCalendarSourceDto.externalCalendarSourceId = req.targetCalendarSourceId;
                    }
                  });
                return draft;
              })
            );
          });
      },
      invalidatesTags: ["v1/calendar/event/filter", "v1/task/list/filter"]
    }),
    //
    updateCalendarEventDates: build.mutation<BaseResponse, CalendarEventDateUpdateRequest>({
      query: (req) => ({ url: `v1/calendar/event/update/from-external/dates`, method: "PUT", body: req }),
      onQueryStarted(req, { dispatch, queryFulfilled, getState }) {
        //cgds-448 might need later
        const invalidatedTaskListFilterCaches = taskListingApi.util.selectInvalidatedBy(getState(), ["v1/task/list/filter"]);
        const invalidatedEventFilterCaches = calendarEventApi.util.selectInvalidatedBy(getState(), ["v1/calendar/event/filter"]);
        invalidatedEventFilterCaches
          .filter((cache) => cache.endpointName == "filterCalendarEvents")
          .forEach((cache) => {
            dispatch(
              calendarEventApi.util.updateQueryData("filterCalendarEvents", cache.originalArgs, (draft) => {
                draft.data
                  .filter((calendarEventDto) => calendarEventDto.calendarEventSourceType != "TASK")
                  .filter(
                    (calendarEventDto) =>
                      calendarEventDto.calendarEventId == req.calendarEventId && calendarEventDto.calendarId == req.calendarId
                  )
                  .forEach((calendarEventDto) => {
                    if (req.assignedDate) {
                      calendarEventDto.assignedDate = req.assignedDate;
                    }
                    if (req.dueDate) {
                      calendarEventDto.dueDate = req.dueDate;
                    }
                    if (req.hasPreciseAssignedDate) {
                      calendarEventDto.hasPreciseAssignedDate = req.hasPreciseAssignedDate;
                    }
                    if (req.hasPreciseDueDate) {
                      calendarEventDto.hasPreciseDueDate = req.hasPreciseDueDate;
                    }
                  });
                return draft;
              })
            );
          });
      },
      invalidatesTags: ["v1/calendar/event/filter", "v1/task/list/filter"]
    }),
    //
    updateTitleAndDescription: build.mutation<BaseResponse, CalendarEventTitleDescriptionUpdateRequest>({
      query: (req) => ({ url: `v1/calendar/event/update/from-external/title-description`, method: "PUT", body: req }),
      invalidatesTags: ["v1/calendar/event/filter", "v1/task/list/filter"]
    }),
    //
    deleteCalendarEvent: build.mutation<BaseResponse, { calendarId: string; sourceId: string, eventId: string, }>({
      query: ({ calendarId, sourceId, eventId }) => ({
        url: `v1/calendar/event/update/from-external/calendar/${calendarId}/source/${sourceId}/event/${eventId}`,
        method: "DELETE"
      }),
      invalidatesTags: ["v1/calendar/event/filter", "v1/task/list/filter"]
    }),
    //
    retrieveShareableKey: build.query<CalendarShareableKeyResponse, { workspaceId: string }>({
      query: ({ workspaceId }: { workspaceId: string }) => `v1/calendar/event/exports/workspace/${workspaceId}/key`,
      providesTags: (_result, _err, req) => [
        {
          type: `v1/calendar/event/exports/workspace/{workspaceId}/key`,
          id: req.workspaceId
        }
      ]
    }),
    //
    refreshShareableKey: build.mutation<BaseResponse, { workspaceId: string }>({
      query: ({ workspaceId }: { workspaceId: string }) => ({
        url: `v1/calendar/event/exports/workspace/${workspaceId}/key/refresh`,
        method: "POST"
      }),
      invalidatesTags: [`v1/calendar/event/exports/workspace/{workspaceId}/key`]
    })
    //
  })
});

export const {
  useFilterCalendarEventsQuery,
  useInsertEventMutation,
  useMoveEventMutation,
  useUpdateCalendarEventDatesMutation,
  useUpdateTitleAndDescriptionMutation,
  useDeleteCalendarEventMutation,
  useRetrieveShareableKeyQuery,
  useRefreshShareableKeyMutation
} = calendarEventApi;

export const {
  endpoints: {
    filterCalendarEvents,
    insertEvent,
    moveEvent,
    updateCalendarEventDates,
    updateTitleAndDescription,
    deleteCalendarEvent,
    retrieveShareableKey,
    refreshShareableKey
  }
} = calendarEventApi;
