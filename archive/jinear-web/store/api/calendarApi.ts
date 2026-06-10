import { BaseResponse } from "@/model/be/jinear-core";
import { api } from "./api";

export const calendarApi = api.injectEndpoints({
  endpoints: (build) => ({
    //
    deleteCalendar: build.mutation<BaseResponse, { calendarId: string }>({
      query: (req: { calendarId: string }) => ({
        url: `v1/calendar/${req.calendarId}`,
        method: "DELETE",
      }),
      invalidatesTags: (_result, _err, req) => ["v1/calendar/member/memberships/{workspaceId}", "v1/calendar/event/filter"],
    }),
    //
  }),
});

export const { useDeleteCalendarMutation } = calendarApi;

export const {
  endpoints: { deleteCalendar },
} = calendarApi;
