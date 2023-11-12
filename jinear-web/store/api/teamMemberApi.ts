import { AddTeamMemberRequest, BaseResponse, TeamMemberListingResponse, TeamMembershipsResponse } from "@/model/be/jinear-core";
import { api } from "./api";

export const teamMemberApi = api.injectEndpoints({
  endpoints: (build) => ({
    retrieveTeamMembers: build.query<TeamMemberListingResponse, { teamId: string; page?: number }>({
      query: ({ teamId, page = 0 }) => `v1/team/member/list/${teamId}?page=${page}`,
      providesTags: (_result, _err, { teamId, page = 0 }) => [
        {
          type: "team-member-list",
          id: `${teamId}-${page}`,
        },
      ],
    }),
    //
    kickMemberFromTeam: build.mutation<BaseResponse, { teamMemberId: string }>({
      query: ({ teamMemberId }) => ({
        url: `v1/team/member/${teamMemberId}`,
        method: "DELETE",
      }),
      invalidatesTags: ["team-member-list", "workspace-activity-list"],
    }),
    //
    addTeamMember: build.mutation<BaseResponse, AddTeamMemberRequest>({
      query: (req) => ({
        url: `v1/team/member`,
        method: "POST",
        body: req,
      }),
      invalidatesTags: ["team-member-list", "workspace-activity-list"],
    }),
    //
    retrieveMemberships: build.query<TeamMembershipsResponse, { workspaceId: string }>({
      query: ({ workspaceId }) => `v1/team/member/memberships/${workspaceId}`,
      providesTags: (_result, _err, { workspaceId }) => [
        {
          type: "workspace-team-membership-list",
          id: `${workspaceId}`,
        },
      ],
    }),
    //
  }),
});

export const {
  useRetrieveTeamMembersQuery,
  useKickMemberFromTeamMutation,
  useAddTeamMemberMutation,
  useRetrieveMembershipsQuery,
} = teamMemberApi;

export const {
  endpoints: { retrieveTeamMembers, kickMemberFromTeam, addTeamMember, retrieveMemberships },
} = teamMemberApi;
