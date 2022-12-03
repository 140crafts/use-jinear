import { TeamListingResponse } from "@/model/be/jinear-core";
import { api } from "./api";

export const teamApi = api.injectEndpoints({
  endpoints: (build) => ({
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

export const { useRetrieveWorkspaceTeamsQuery } = teamApi;

export const {
  endpoints: { retrieveWorkspaceTeams },
} = teamApi;
