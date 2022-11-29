import { TeamMemberListingResponse } from "@/model/be/jinear-core";
import { api } from "./api";

export const teamMemberApi = api.injectEndpoints({
  endpoints: (build) => ({
    retrieveTeamMembers: build.query<TeamMemberListingResponse, string>({
      query: (teamId: string) => `v1/team/member/${teamId}/list`,
      providesTags: (_result, _err, teamId) => [
        {
          type: "team-member-list",
          id: teamId,
        },
      ],
    }),
  }),
});

export const { useRetrieveTeamMembersQuery } = teamMemberApi;

export const {
  endpoints: { retrieveTeamMembers },
} = teamMemberApi;
