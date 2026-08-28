import type {
    AdminWorkspaceInitializeRequest,
    AdminWorkspaceListingResponse,
    AdminWorkspaceMemberAddRequest,
    BaseResponse,
    WorkspaceBaseResponse,
    WorkspaceMemberListingBaseResponse,
    WorkspaceTier,
    WorkspaceTitleUpdateRequest
} from "@/model/be/jinear-core";
import {api} from "./api";

export const adminWorkspaceApi = api.injectEndpoints({
    endpoints: (build) => ({
        adminRetrieveWorkspaces: build.query<AdminWorkspaceListingResponse, { page?: number }>({
            query: ({page = 0}) => `v1/admin/workspace/list?page=${page}`,
            providesTags: (_result, _err, {page = 0}) => [
                {
                    type: "v1/admin/workspace/list",
                    id: `${page}`,
                },
            ],
        }),
        //
        adminInitializeWorkspace: build.mutation<WorkspaceBaseResponse, AdminWorkspaceInitializeRequest>({
            query: (body: AdminWorkspaceInitializeRequest) => ({
                url: `v1/admin/workspace`,
                method: "POST",
                body
            }),
            invalidatesTags: ["v1/admin/workspace/list", "v1/admin/account/{accountId}/workspaces"]
        }),
        //
        adminUpdateWorkspaceTitle: build.mutation<BaseResponse, {
            workspaceId: string;
            body: WorkspaceTitleUpdateRequest
        }>({
            query: ({workspaceId, body}) => ({
                url: `v1/admin/workspace/${workspaceId}/title`,
                method: "PUT",
                body
            }),
            invalidatesTags: ["v1/admin/workspace/list"]
        }),
        //
        adminUpdateWorkspaceTier: build.mutation<BaseResponse, {
            workspaceId: string;
            workspaceTier: WorkspaceTier
        }>({
            query: ({workspaceId, workspaceTier}) => ({
                url: `v1/admin/workspace/${workspaceId}/tier/${workspaceTier}`,
                method: "PUT",
            }),
            invalidatesTags: ["v1/admin/workspace/list"]
        }),
        //
        adminDeleteWorkspace: build.mutation<BaseResponse, { workspaceId: string }>({
            query: ({workspaceId}) => ({
                url: `v1/admin/workspace/${workspaceId}`,
                method: "DELETE",
            }),
            invalidatesTags: [
                "v1/admin/workspace/list",
                "v1/admin/team/list/{workspaceId}",
                "v1/admin/account/{accountId}/workspaces",
                "v1/admin/account/{accountId}/teams"
            ]
        }),
        //
        adminRetrieveWorkspaceMembers: build.query<WorkspaceMemberListingBaseResponse, {
            workspaceId: string;
            page?: number
        }>({
            query: ({workspaceId, page = 0}) => `v1/admin/workspace/${workspaceId}/member/list?page=${page}`,
            providesTags: (_result, _err, {workspaceId, page = 0}) => [
                {
                    type: "v1/admin/workspace/{workspaceId}/member/list",
                    id: `${workspaceId}-${page}`,
                },
            ],
        }),
        //
        adminAddWorkspaceMember: build.mutation<BaseResponse, {
            workspaceId: string;
            body: AdminWorkspaceMemberAddRequest
        }>({
            query: ({workspaceId, body}) => ({
                url: `v1/admin/workspace/${workspaceId}/member`,
                method: "POST",
                body
            }),
            invalidatesTags: [
                "v1/admin/workspace/{workspaceId}/member/list",
                "v1/admin/account/{accountId}/workspaces"
            ]
        }),
        //
        adminRemoveWorkspaceMember: build.mutation<BaseResponse, {
            workspaceId: string;
            workspaceMemberId: string
        }>({
            query: ({workspaceId, workspaceMemberId}) => ({
                url: `v1/admin/workspace/${workspaceId}/member/${workspaceMemberId}`,
                method: "DELETE",
            }),
            invalidatesTags: [
                "v1/admin/workspace/{workspaceId}/member/list",
                "v1/admin/team/{teamId}/member/list",
                "v1/admin/account/{accountId}/workspaces",
                "v1/admin/account/{accountId}/teams"
            ]
        }),
        //
    }),
});

export const {
    useAdminRetrieveWorkspacesQuery,
    useAdminInitializeWorkspaceMutation,
    useAdminUpdateWorkspaceTitleMutation,
    useAdminUpdateWorkspaceTierMutation,
    useAdminDeleteWorkspaceMutation,
    useAdminRetrieveWorkspaceMembersQuery,
    useAdminAddWorkspaceMemberMutation,
    useAdminRemoveWorkspaceMemberMutation
} = adminWorkspaceApi;

export const {
    endpoints: {
        adminRetrieveWorkspaces,
        adminInitializeWorkspace,
        adminUpdateWorkspaceTitle,
        adminUpdateWorkspaceTier,
        adminDeleteWorkspace,
        adminRetrieveWorkspaceMembers,
        adminAddWorkspaceMember,
        adminRemoveWorkspaceMember
    },
} = adminWorkspaceApi;
