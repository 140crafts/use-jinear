import type {
    AccountRetrieveResponse,
    AccountWorkspacesResponse,
    AdminAccountCreateRequest,
    AdminAccountListingResponse,
    AdminAccountPasswordUpdateRequest,
    BaseResponse,
    TeamMembershipsResponse
} from "@/model/be/jinear-core";
import {api} from "./api";

export const adminAccountApi = api.injectEndpoints({
    endpoints: (build) => ({
        adminRetrieveAccounts: build.query<AdminAccountListingResponse, { page?: number }>({
            query: ({page = 0}) => `v1/admin/account/list?page=${page}`,
            providesTags: (_result, _err, {page = 0}) => [
                {
                    type: "v1/admin/account/list",
                    id: `${page}`,
                },
            ],
        }),
        //
        adminCreateAccount: build.mutation<AccountRetrieveResponse, AdminAccountCreateRequest>({
            query: (body: AdminAccountCreateRequest) => ({
                url: `v1/admin/account`,
                method: "POST",
                body
            }),
            invalidatesTags: ["v1/admin/account/list"]
        }),
        //
        adminUpdateAccountPassword: build.mutation<BaseResponse, {
            accountId: string;
            body: AdminAccountPasswordUpdateRequest
        }>({
            query: ({accountId, body}) => ({
                url: `v1/admin/account/${accountId}/password`,
                method: "PUT",
                body
            }),
        }),
        //
        adminRetrieveAccountWorkspaces: build.query<AccountWorkspacesResponse, { accountId: string }>({
            query: ({accountId}) => `v1/admin/account/${accountId}/workspaces`,
            providesTags: (_result, _err, {accountId}) => [
                {
                    type: "v1/admin/account/{accountId}/workspaces",
                    id: accountId,
                },
            ],
        }),
        //
        adminRetrieveAccountTeams: build.query<TeamMembershipsResponse, { accountId: string }>({
            query: ({accountId}) => `v1/admin/account/${accountId}/teams`,
            providesTags: (_result, _err, {accountId}) => [
                {
                    type: "v1/admin/account/{accountId}/teams",
                    id: accountId,
                },
            ],
        }),
        //
    }),
});

export const {
    useAdminRetrieveAccountsQuery,
    useAdminCreateAccountMutation,
    useAdminUpdateAccountPasswordMutation,
    useAdminRetrieveAccountWorkspacesQuery,
    useAdminRetrieveAccountTeamsQuery
} = adminAccountApi;

export const {
    endpoints: {
        adminRetrieveAccounts,
        adminCreateAccount,
        adminUpdateAccountPassword,
        adminRetrieveAccountWorkspaces,
        adminRetrieveAccountTeams
    },
} = adminAccountApi;
