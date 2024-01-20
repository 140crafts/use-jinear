import {
  BaseResponse,
  WorkspaceInvitationInfoResponse,
  WorkspaceInvitationListingResponse,
  WorkspaceMemberInvitationRespondRequest,
  WorkspaceMemberInviteRequest,
} from "@/model/be/jinear-core";
import { api } from "./api";

export const workspaceMemberInvitationApi = api.injectEndpoints({
  endpoints: (build) => ({
    //
    listInvitations: build.query<WorkspaceInvitationListingResponse, { workspaceId: string; page?: number }>({
      query: ({ workspaceId, page = 0 }) => `v1/workspace/member/invitation/list/${workspaceId}?page=${page}`,
      providesTags: (_result, _err, { workspaceId, page = 0 }) => [
        {
          type: "v1/workspace/member/invitation/list/{workspaceId}",
          id: `${workspaceId}-${page}`,
        },
      ],
    }),
    //
    inviteWorkspace: build.mutation<BaseResponse, WorkspaceMemberInviteRequest>({
      query: (body) => ({
        url: `v1/workspace/member/invitation`,
        method: "POST",
        body,
      }),
      invalidatesTags: ["v1/workspace/member/invitation/list/{workspaceId}"],
    }),
    //
    deleteInvitation: build.mutation<BaseResponse, { invitationId: string }>({
      query: ({ invitationId }) => ({
        url: `v1/workspace/member/invitation/${invitationId}`,
        method: "DELETE",
      }),
      invalidatesTags: ["v1/workspace/member/invitation/list/{workspaceId}"],
    }),
    //
    retrieveInvitationInfo: build.query<WorkspaceInvitationInfoResponse, string>({
      query: (token: string) => `v1/workspace/member/invitation/info/${token}`,
      providesTags: (_result, _err, token) => [
        {
          type: "v1/workspace/member/invitation/info/{token}",
          id: token,
        },
      ],
    }),
    //
    respondInvitation: build.mutation<BaseResponse, WorkspaceMemberInvitationRespondRequest>({
      query: (body) => ({
        url: `v1/workspace/member/invitation/respond`,
        method: "POST",
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

export const {
  useListInvitationsQuery,
  useInviteWorkspaceMutation,
  useDeleteInvitationMutation,
  useRetrieveInvitationInfoQuery,
  useRespondInvitationMutation,
} = workspaceMemberInvitationApi;

export const {
  endpoints: { listInvitations, inviteWorkspace, deleteInvitation, retrieveInvitationInfo, respondInvitation },
} = workspaceMemberInvitationApi;
