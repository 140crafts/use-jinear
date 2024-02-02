import { BaseResponse, CalendarMemberListingResponse, CalendarMemberPaginatedResponse } from "@/model/be/jinear-core";
import { api } from "./api";

export const calendarMemberApi = api.injectEndpoints({
  endpoints: (build) => ({
    //
    retrieveCalendarMemberships: build.query<CalendarMemberListingResponse, { workspaceId: string }>({
      query: (req: { workspaceId: string }) => `v1/calendar/member/memberships/${req.workspaceId}`,
      providesTags: (_result, _err, req) => [
        {
          type: `v1/calendar/member/memberships/{workspaceId}`,
          id: `${req.workspaceId}`,
        },
      ],
    }),
    //
    retrieveCalendarMembers: build.query<CalendarMemberPaginatedResponse, { calendarId: string }>({
      query: (req: { calendarId: string }) => `v1/calendar/member/list/${req.calendarId}`,
      providesTags: (_result, _err, req) => [
        {
          type: `v1/calendar/member/list/{calendarId}`,
          id: `${req.calendarId}`,
        },
      ],
    }),
    //
    addCalendarMember: build.mutation<BaseResponse, { calendarId: string; accountId: string }>({
      query: ({ calendarId, accountId }: { calendarId: string; accountId: string }) => ({
        url: `v1/calendar/${calendarId}/manage/${accountId}`,
        method: "POST",
      }),
      invalidatesTags: (_result, _err, req) => [
        `v1/calendar/member/memberships/{workspaceId}`,
        `v1/calendar/member/list/{calendarId}`,
      ],
    }),
    //
    kickCalendarMember: build.mutation<BaseResponse, { calendarId: string; accountId: string }>({
      query: ({ calendarId, accountId }: { calendarId: string; accountId: string }) => ({
        url: `v1/calendar/${calendarId}/manage/${accountId}`,
        method: "DELETE",
      }),
      invalidatesTags: (_result, _err, req) => [
        `v1/calendar/member/memberships/{workspaceId}`,
        `v1/calendar/member/list/{calendarId}`,
      ],
    }),
    //
  }),
});

export const {
  useRetrieveCalendarMembershipsQuery,
  useRetrieveCalendarMembersQuery,
  useAddCalendarMemberMutation,
  useKickCalendarMemberMutation,
} = calendarMemberApi;

export const {
  endpoints: { retrieveCalendarMemberships, retrieveCalendarMembers, addCalendarMember, kickCalendarMember },
} = calendarMemberApi;
