import { BaseResponse } from "@/model/be/jinear-core";
import { api } from "./api";

export const workspaceDisplayPreferenceApi = api.injectEndpoints({
  endpoints: (build) => ({
    updatePreferredWorkspace: build.mutation<
      BaseResponse,
      { workspaceId: string }
    >({
      query: (body: { workspaceId: string }) => ({
        url: `v1/workspace/display-preferences/${body.workspaceId}/set-preferred`,
        method: "PUT",
        body,
      }),
      invalidatesTags: [
        "Account-Current",
        "workplace-member-list",
        "workplace-team-list",
        "team-member-list",
        "team-topic-list",
      ],
    }),
    updatePreferredTeam: build.mutation<
      BaseResponse,
      { workspaceId: string; teamId: string }
    >({
      query: (body: { workspaceId: string; teamId: string }) => ({
        url: `v1/workspace/display-preferences/${body.workspaceId}/set-preferred-team/${body.teamId}`,
        method: "PUT",
        body,
      }),
      invalidatesTags: [
        "Account-Current",
        "team-member-list",
        "team-topic-list",
      ],
    }),
  }),
});

export const {
  useUpdatePreferredTeamMutation,
  useUpdatePreferredWorkspaceMutation,
} = workspaceDisplayPreferenceApi;

export const {
  endpoints: { updatePreferredWorkspace, updatePreferredTeam },
} = workspaceDisplayPreferenceApi;
