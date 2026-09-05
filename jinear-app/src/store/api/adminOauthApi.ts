import type {BaseResponse, OauthClientListingResponse} from "@/model/be/jinear-core";
import {api} from "./api";

export const adminOauthApi = api.injectEndpoints({
    endpoints: (build) => ({
        adminRetrieveOauthClients: build.query<OauthClientListingResponse, { page?: number }>({
            query: ({page = 0}) => `v1/admin/oauth/client/list?page=${page}`,
            providesTags: (_result, _err, {page = 0}) => [
                {
                    type: "v1/admin/oauth/client/list",
                    id: `${page}`,
                },
            ],
        }),
        //
        adminRevokeOauthClient: build.mutation<BaseResponse, { clientId: string }>({
            query: ({clientId}) => ({
                url: `v1/admin/oauth/client?clientId=${encodeURIComponent(clientId)}`,
                method: "DELETE",
            }),
            invalidatesTags: [
                "v1/admin/oauth/client/list",
                "v1/admin/mcp/analytics",
                "v1/oauth/connection/list",
            ],
        }),
        //
    }),
});

export const {
    useAdminRetrieveOauthClientsQuery,
    useAdminRevokeOauthClientMutation,
} = adminOauthApi;
