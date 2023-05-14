import { WorkspaceDisplayPreferenceResponse } from "@/model/be/jinear-core";
import { api } from "./api";

export const workspaceDisplayPreferenceApi = api.injectEndpoints({
  endpoints: (build) => ({
    updatePreferredWorkspace: build.mutation<WorkspaceDisplayPreferenceResponse, { workspaceId: string; dontReroute?: boolean }>({
      query: (body: { workspaceId: string }) => ({
        url: `v1/workspace/display-preferences/${body.workspaceId}/set-preferred`,
        method: "PUT",
        body,
      }),
      invalidatesTags: ["Account-Current", "workplace-member-list", "workplace-team-list", "team-member-list", "team-topic-list"],
    }),
    updatePreferredTeam: build.mutation<
      WorkspaceDisplayPreferenceResponse,
      { workspaceId: string; teamId: string; dontReroute?: boolean }
    >({
      query: (body: { workspaceId: string; teamId: string }) => ({
        url: `v1/workspace/display-preferences/${body.workspaceId}/set-preferred-team/${body.teamId}`,
        method: "PUT",
        body,
      }),
      invalidatesTags: ["Account-Current", "team-member-list", "team-topic-list"],
    }),
    updatePreferredWorkspaceWithUsername: build.mutation<
      WorkspaceDisplayPreferenceResponse,
      { workspaceUsername: string; dontReroute?: boolean }
    >({
      query: (body: { workspaceUsername: string }) => ({
        url: `v1/workspace/display-preferences/with-username/${body.workspaceUsername}/set-preferred`,
        method: "PUT",
        body,
      }),
      invalidatesTags: ["Account-Current", "workplace-member-list", "workplace-team-list", "team-member-list", "team-topic-list"],
    }),
    updatePreferredTeamWithUsername: build.mutation<
      WorkspaceDisplayPreferenceResponse,
      { workspaceUsername: string; teamUsername: string; dontReroute?: boolean }
    >({
      query: (body: { workspaceUsername: string; teamUsername: string }) => ({
        url: `v1/workspace/display-preferences/with-username/${body.workspaceUsername}/set-preferred-team/${body.teamUsername}`,
        method: "PUT",
        body,
      }),
      invalidatesTags: ["Account-Current", "team-member-list", "team-topic-list"],
    }),
  }),
});

export const {
  useUpdatePreferredTeamMutation,
  useUpdatePreferredWorkspaceMutation,
  useUpdatePreferredWorkspaceWithUsernameMutation,
  useUpdatePreferredTeamWithUsernameMutation,
} = workspaceDisplayPreferenceApi;

export const {
  endpoints: {
    updatePreferredWorkspace,
    updatePreferredTeam,
    updatePreferredWorkspaceWithUsername,
    updatePreferredTeamWithUsername,
  },
} = workspaceDisplayPreferenceApi;
