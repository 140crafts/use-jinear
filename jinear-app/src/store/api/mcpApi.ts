import type {McpServerInfoResponse} from "@/model/be/jinear-core";
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
    }),
});

export const {useRetrieveMcpServerInfoQuery} = mcpApi;
