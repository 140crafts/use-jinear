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
      invalidatesTags: [
        "v1/account",
        "v1/workspace/member/{workspaceId}/list",
        "v1/team/from-workspace/{workspaceId}",
        "v1/team/member/list/{teamId}",
        "v1/topic/list/{teamId}",
      ],
    }),
    //
    updatePreferredWorkspaceWithUsername: build.mutation<
      WorkspaceDisplayPreferenceResponse,
      { workspaceUsername: string; dontReroute?: boolean }
    >({
      query: (body: { workspaceUsername: string }) => ({
        url: `v1/workspace/display-preferences/with-username/${body.workspaceUsername}/set-preferred`,
        method: "PUT",
        body,
      }),
      invalidatesTags: [
        "v1/account",
        "v1/workspace/member/{workspaceId}/list",
        "v1/team/from-workspace/{workspaceId}",
        "v1/team/member/list/{teamId}",
        "v1/topic/list/{teamId}",
      ],
    }),
    //
  }),
});

export const { useUpdatePreferredWorkspaceMutation, useUpdatePreferredWorkspaceWithUsernameMutation } =
  workspaceDisplayPreferenceApi;

export const {
  endpoints: { updatePreferredWorkspace, updatePreferredWorkspaceWithUsername },
} = workspaceDisplayPreferenceApi;
