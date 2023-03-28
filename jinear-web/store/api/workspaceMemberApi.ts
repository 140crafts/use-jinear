import {
  BaseResponse,
  WorkspaceInvitationInfoResponse,
  WorkspaceMemberInvitationRespondRequest,
  WorkspaceMemberInviteRequest,
  WorkspaceMemberListingBaseResponse,
} from "@/model/be/jinear-core";
import { api } from "./api";

export const workplaceMemberApi = api.injectEndpoints({
  endpoints: (build) => ({
    retrieveWorkspaceMembers: build.query<WorkspaceMemberListingBaseResponse, string>({
      query: (workspaceId: string) => `v1/workspace/member/${workspaceId}/list`,
      providesTags: (_result, _err, workspaceId) => [
        {
          type: "workplace-member-list",
          id: workspaceId,
        },
      ],
    }),
    //
    inviteWorkspace: build.mutation<BaseResponse, WorkspaceMemberInviteRequest>({
      query: (body) => ({
        url: `v1/workspace/member/invite`,
        method: "POST",
        body,
      }),
      invalidatesTags: [""], //TODO workspace-invitations
    }),
    //
    retrieveInvitationInfo: build.query<WorkspaceInvitationInfoResponse, string>({
      query: (token: string) => `v1/workspace/member/invitation-info/${token}`,
      providesTags: (_result, _err, token) => [
        {
          type: "workspace-invitation-info",
          id: token,
        },
      ],
    }),
    //
    respondInvitation: build.mutation<BaseResponse, WorkspaceMemberInvitationRespondRequest>({
      query: (body) => ({
        url: `v1/workspace/member/respond-invitation`,
        method: "POST",
        body,
      }),
      invalidatesTags: ["Account-Current", "workplace-member-list", "workplace-team-list", "team-member-list", "team-topic-list"],
    }),
    //
  }),
});

export const {
  useRetrieveWorkspaceMembersQuery,
  useInviteWorkspaceMutation,
  useRetrieveInvitationInfoQuery,
  useRespondInvitationMutation,
} = workplaceMemberApi;

export const {
  endpoints: { retrieveWorkspaceMembers, inviteWorkspace, retrieveInvitationInfo, respondInvitation },
} = workplaceMemberApi;
