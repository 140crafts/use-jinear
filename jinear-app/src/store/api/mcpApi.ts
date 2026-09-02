import type {
    BaseResponse,
    McpConnectionListingResponse,
    McpConsentInfoResponse,
    McpConsentResponse,
    McpServerInfoResponse,
} from "@/model/be/jinear-core";
import {api} from "./api";

export const mcpApi = api.injectEndpoints({
    endpoints: (build) => ({
        retrieveMcpServerInfo: build.query<McpServerInfoResponse, void>({
            query: () => `v1/mcp/info`,
            providesTags: () => [
                {
                    type: "v1/mcp/info",
                },
            ],
        }),
        //
        retrieveMcpConnections: build.query<McpConnectionListingResponse, void>({
            query: () => `v1/mcp/connection/list`,
            providesTags: () => [
                {
                    type: "v1/mcp/connection/list",
                },
            ],
        }),
        //
        revokeMcpConnection: build.mutation<BaseResponse, { mcpConnectionId: string }>({
            query: (req: { mcpConnectionId: string }) => ({
                url: `v1/mcp/connection/${req.mcpConnectionId}`,
                method: "DELETE",
            }),
            invalidatesTags: () => [
                {
                    type: "v1/mcp/connection/list",
                },
            ],
        }),
        //
        retrieveMcpConsentInfo: build.query<McpConsentInfoResponse, { requestId: string }>({
            query: (req: { requestId: string }) => `v1/oauth/authorize/info/${req.requestId}`,
            providesTags: (_result, _err, req) => [
                {
                    type: "v1/oauth/authorize/info/{requestId}",
                    id: req.requestId,
                },
            ],
        }),
        //
        submitMcpConsent: build.mutation<McpConsentResponse, { requestId: string; approved: boolean }>({
            query: (req: { requestId: string; approved: boolean }) => ({
                url: `v1/oauth/authorize/consent`,
                method: "POST",
                body: req,
            }),
            invalidatesTags: (_result, _err, req) => [
                {
                    type: "v1/oauth/authorize/info/{requestId}",
                    id: req.requestId,
                },
                {
                    type: "v1/mcp/connection/list",
                },
            ],
        }),
        //
    }),
});

export const {
    useRetrieveMcpServerInfoQuery,
    useRetrieveMcpConnectionsQuery,
    useRevokeMcpConnectionMutation,
    useRetrieveMcpConsentInfoQuery,
    useSubmitMcpConsentMutation,
} = mcpApi;
