import { TeamInitializeRequest, TeamListingResponse, TeamResponse } from "@/model/be/jinear-core";
import { api } from "./api";

export const teamApi = api.injectEndpoints({
  endpoints: (build) => ({
    initializeTeam: build.mutation<TeamResponse, TeamInitializeRequest>({
      query: (body: TeamInitializeRequest) => ({
        url: `v1/team`,
        method: "POST",
        body,
      }),
      invalidatesTags: ["workplace-team-list"],
    }),
    retrieveWorkspaceTeams: build.query<TeamListingResponse, string>({
      query: (workspaceId: string) => `v1/team/from-workspace/${workspaceId}`,
      providesTags: (_result, _err, workspaceId) => [
        {
          type: "workplace-team-list",
          id: workspaceId,
        },
        {
          type: "workplace-team-list",
          id: "ALL",
        },
      ],
    }),
  }),
});

export const { useInitializeTeamMutation, useRetrieveWorkspaceTeamsQuery } = teamApi;

export const {
  endpoints: { initializeTeam, retrieveWorkspaceTeams },
} = teamApi;
