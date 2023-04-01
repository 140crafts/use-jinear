import { TeamMemberListingResponse } from "@/model/be/jinear-core";
import { api } from "./api";

export const teamMemberApi = api.injectEndpoints({
  endpoints: (build) => ({
    retrieveTeamMembers: build.query<TeamMemberListingResponse, { teamId: string; page?: number }>({
      query: ({ teamId, page = 0 }) => `v1/team/member/${teamId}/list?page=${page}`,
      providesTags: (_result, _err, { teamId, page = 0 }) => [
        {
          type: "team-member-list",
          id: `${teamId}-${page}`,
        },
      ],
    }),
  }),
});

export const { useRetrieveTeamMembersQuery } = teamMemberApi;

export const {
  endpoints: { retrieveTeamMembers },
} = teamMemberApi;
