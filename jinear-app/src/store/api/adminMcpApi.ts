import type {
    BaseResponse,
    McpAnalyticsResponse,
    McpOauthClientListingResponse,
    McpToolCallLogListingResponse,
} from "@/model/be/jinear-core";
import {api} from "./api";

export const adminMcpApi = api.injectEndpoints({
    endpoints: (build) => ({
        adminRetrieveMcpAnalytics: build.query<McpAnalyticsResponse, void>({
            query: () => `v1/admin/mcp/analytics`,
            providesTags: () => [
                {
                    type: "v1/admin/mcp/analytics",
                },
            ],
        }),
        //
        adminRetrieveMcpClients: build.query<McpOauthClientListingResponse, { page?: number }>({
            query: ({page = 0}) => `v1/admin/mcp/client/list?page=${page}`,
            providesTags: (_result, _err, {page = 0}) => [
                {
                    type: "v1/admin/mcp/client/list",
                    id: `${page}`,
                },
            ],
        }),
        //
        adminRetrieveMcpLogs: build.query<McpToolCallLogListingResponse, { page?: number }>({
            query: ({page = 0}) => `v1/admin/mcp/log/list?page=${page}`,
            providesTags: (_result, _err, {page = 0}) => [
                {
                    type: "v1/admin/mcp/log/list",
                    id: `${page}`,
                },
            ],
        }),
        //
        adminRevokeMcpClient: build.mutation<BaseResponse, { clientId: string }>({
            query: ({clientId}) => ({
                url: `v1/admin/mcp/client?clientId=${encodeURIComponent(clientId)}`,
                method: "DELETE",
            }),
            invalidatesTags: [
                "v1/admin/mcp/client/list",
                "v1/admin/mcp/analytics",
                "v1/mcp/connection/list",
            ],
        }),
        //
    }),
});

export const {
    useAdminRetrieveMcpAnalyticsQuery,
    useAdminRetrieveMcpClientsQuery,
    useAdminRetrieveMcpLogsQuery,
    useAdminRevokeMcpClientMutation,
} = adminMcpApi;
