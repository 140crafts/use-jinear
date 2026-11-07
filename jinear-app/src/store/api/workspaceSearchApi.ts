import type {WorkspaceSearchRequest, WorkspaceSearchResponse} from "@/model/be/jinear-core";
import {api} from "./api";

export const workspaceSearchApi = api.injectEndpoints({
    endpoints: (build) => ({
        searchWorkspace: build.query<WorkspaceSearchResponse, { body: WorkspaceSearchRequest; page?: number }>({
            query: (req) => {
                const page = req.page ? req.page : 0;
                return {
                    url: `v1/workspace/search?page=${page}`,
                    method: "POST",
                    body: req.body
                };
            },
            providesTags: (_result, _err, req) => [
                {
                    type: "v1/workspace/search",
                    id: `${JSON.stringify(req)}`
                }
            ]
        })
        //
    })
});

export const {useSearchWorkspaceQuery, useLazySearchWorkspaceQuery} = workspaceSearchApi;

export const {
    endpoints: {searchWorkspace}
} = workspaceSearchApi;
