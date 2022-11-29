import { WorkspaceMemberListingBaseResponse } from "@/model/be/jinear-core";
import { api } from "./api";

export const workplaceMemberApi = api.injectEndpoints({
  endpoints: (build) => ({
    retrieveWorkspaceMembers: build.query<
      WorkspaceMemberListingBaseResponse,
      string
    >({
      query: (workspaceId: string) => `v1/workspace/member/${workspaceId}/list`,
      providesTags: (_result, _err, workspaceId) => [
        {
          type: "workplace-member-list",
          id: workspaceId,
        },
      ],
    }),
  }),
});

export const { useRetrieveWorkspaceMembersQuery } = workplaceMemberApi;

export const {
  endpoints: { retrieveWorkspaceMembers },
} = workplaceMemberApi;
