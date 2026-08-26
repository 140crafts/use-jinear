import type {
    AdminTeamMemberAddRequest,
    AdminTeamRenameRequest,
    BaseResponse,
    TeamInitializeRequest,
    TeamListingResponse,
    TeamMemberListingResponse,
    TeamResponse
} from "@/model/be/jinear-core";
import {api} from "./api";

export const adminTeamApi = api.injectEndpoints({
    endpoints: (build) => ({
        adminRetrieveWorkspaceTeams: build.query<TeamListingResponse, { workspaceId: string }>({
            query: ({workspaceId}) => `v1/admin/team/list/${workspaceId}`,
            providesTags: (_result, _err, {workspaceId}) => [
                {
                    type: "v1/admin/team/list/{workspaceId}",
                    id: workspaceId,
                },
            ],
        }),
        //
        adminInitializeTeam: build.mutation<TeamResponse, TeamInitializeRequest>({
            query: (body: TeamInitializeRequest) => ({
                url: `v1/admin/team`,
                method: "POST",
                body
            }),
            invalidatesTags: ["v1/admin/team/list/{workspaceId}"]
        }),
        //
        adminRenameTeam: build.mutation<BaseResponse, { teamId: string; body: AdminTeamRenameRequest }>({
            query: ({teamId, body}) => ({
                url: `v1/admin/team/${teamId}/name`,
                method: "PUT",
                body
            }),
            invalidatesTags: ["v1/admin/team/list/{workspaceId}", "v1/admin/account/{accountId}/teams"]
        }),
        //
        adminDeleteTeam: build.mutation<BaseResponse, { teamId: string }>({
            query: ({teamId}) => ({
                url: `v1/admin/team/${teamId}`,
                method: "DELETE",
            }),
            invalidatesTags: ["v1/admin/team/list/{workspaceId}", "v1/admin/account/{accountId}/teams"]
        }),
        //
        adminRetrieveTeamMembers: build.query<TeamMemberListingResponse, { teamId: string; page?: number }>({
            query: ({teamId, page = 0}) => `v1/admin/team/${teamId}/member/list?page=${page}`,
            providesTags: (_result, _err, {teamId, page = 0}) => [
                {
                    type: "v1/admin/team/{teamId}/member/list",
                    id: `${teamId}-${page}`,
                },
            ],
        }),
        //
        adminAddTeamMember: build.mutation<BaseResponse, { teamId: string; body: AdminTeamMemberAddRequest }>({
            query: ({teamId, body}) => ({
                url: `v1/admin/team/${teamId}/member`,
                method: "POST",
                body
            }),
            invalidatesTags: ["v1/admin/team/{teamId}/member/list", "v1/admin/account/{accountId}/teams"]
        }),
        //
        adminRemoveTeamMember: build.mutation<BaseResponse, { teamMemberId: string }>({
            query: ({teamMemberId}) => ({
                url: `v1/admin/team/member/${teamMemberId}`,
                method: "DELETE",
            }),
            invalidatesTags: ["v1/admin/team/{teamId}/member/list", "v1/admin/account/{accountId}/teams"]
        }),
        //
    }),
});

export const {
    useAdminRetrieveWorkspaceTeamsQuery,
    useAdminInitializeTeamMutation,
    useAdminRenameTeamMutation,
    useAdminDeleteTeamMutation,
    useAdminRetrieveTeamMembersQuery,
    useAdminAddTeamMemberMutation,
    useAdminRemoveTeamMemberMutation
} = adminTeamApi;

export const {
    endpoints: {
        adminRetrieveWorkspaceTeams,
        adminInitializeTeam,
        adminRenameTeam,
        adminDeleteTeam,
        adminRetrieveTeamMembers,
        adminAddTeamMember,
        adminRemoveTeamMember
    },
} = adminTeamApi;
