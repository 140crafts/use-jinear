import type {MaterialPaginatedResponse, MaterialTextSearchRequest} from "@/model/be/jinear-core";
import {api} from "./api";

export const materialSearchApi = api.injectEndpoints({
    endpoints: (build) => ({
        searchMaterials: build.query<MaterialPaginatedResponse, { workspaceId: string; body: MaterialTextSearchRequest; page?: number }>({
            query: (req) => {
                const page = req.page ? req.page : 0;
                return {
                    url: `v1/material/workspace/${req.workspaceId}/search?page=${page}`,
                    method: "POST",
                    body: req.body
                };
            },
            providesTags: (_result, _err, req) => [
                {
                    type: "v1/material/workspace/{workspaceId}/search",
                    id: `${JSON.stringify(req)}`
                }
            ]
        })
        //
    })
});

export const {useSearchMaterialsQuery, useLazySearchMaterialsQuery} = materialSearchApi;

export const {
    endpoints: {searchMaterials}
} = materialSearchApi;
