import {
  BaseResponse,
  CalendarEventDateUpdateRequest,
  CalendarEventFilterRequest,
  CalendarEventListingResponse,
  CalendarEventTitleDescriptionUpdateRequest,
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
  }),
});

export const { useFilterCalendarEventsQuery, useUpdateCalendarEventDatesMutation, useUpdateTitleAndDescriptionMutation } =
  calendarEventApi;

export const {
  endpoints: { filterCalendarEvents, updateCalendarEventDates, updateTitleAndDescription },
} = calendarEventApi;
