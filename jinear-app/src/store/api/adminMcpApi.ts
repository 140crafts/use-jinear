import type {McpAnalyticsResponse, McpToolCallLogListingResponse} from "@/model/be/jinear-core";
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
    }),
});

export const {
    useAdminRetrieveMcpAnalyticsQuery,
    useAdminRetrieveMcpLogsQuery,
} = adminMcpApi;
