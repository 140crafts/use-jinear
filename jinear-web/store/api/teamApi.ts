import { TeamInitializeRequest, TeamListingResponse, TeamResponse, TeamTaskVisibilityType } from "@/model/be/jinear-core";
import { api, tagTypes } from "./api";

export const teamApi = api.injectEndpoints({
  endpoints: (build) => ({
    initializeTeam: build.mutation<TeamResponse, TeamInitializeRequest>({
      query: (body: TeamInitializeRequest) => ({
        url: `v1/team`,
        method: "POST",
        body,
      }),
      invalidatesTags: ["workplace-team-list", "workspace-activity-list"],
    }),
    //
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
    //
    updateTeamTaskVisibilityType: build.mutation<TeamResponse, { teamId: string; taskVisibilityType: TeamTaskVisibilityType }>({
      query: ({ teamId, taskVisibilityType }) => ({
        url: `v1/team/${teamId}/task-visibility-type/${taskVisibilityType}`,
        method: "PUT",
      }),
      invalidatesTags: tagTypes,
    }),
    //
  }),
});

export const { useInitializeTeamMutation, useRetrieveWorkspaceTeamsQuery, useUpdateTeamTaskVisibilityTypeMutation } = teamApi;

export const {
  endpoints: { initializeTeam, retrieveWorkspaceTeams, updateTeamTaskVisibilityType },
} = teamApi;
