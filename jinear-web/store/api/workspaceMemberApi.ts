import { BaseResponse, WorkspaceMemberListingBaseResponse } from "@/model/be/jinear-core";
import { api } from "./api";

export const workplaceMemberApi = api.injectEndpoints({
  endpoints: (build) => ({
    retrieveWorkspaceMembers: build.query<WorkspaceMemberListingBaseResponse, { workspaceId: string; page?: number }>({
      query: ({ workspaceId, page = 0 }) => `v1/workspace/member/${workspaceId}/list?page=${page}`,
      providesTags: (_result, _err, { workspaceId, page = 0 }) => [
        {
          type: "v1/workspace/member/{workspaceId}/list",
          id: `${workspaceId}-${page}}`,
        },
      ],
    }),
    //
    kickMemberFromWorkspace: build.mutation<BaseResponse, { workspaceId: string; workspaceMemberId: string }>({
      query: ({ workspaceId, workspaceMemberId }) => ({
        url: `v1/workspace/member/${workspaceId}/kick/${workspaceMemberId}`,
        method: "DELETE",
      }),
      invalidatesTags: ["v1/workspace/member/{workspaceId}/list"],
    }),
    //
  }),
});

export const { useRetrieveWorkspaceMembersQuery, useKickMemberFromWorkspaceMutation } = workplaceMemberApi;

export const {
  endpoints: { retrieveWorkspaceMembers, kickMemberFromWorkspace },
} = workplaceMemberApi;
