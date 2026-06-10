import type {MaterialSearchRequest, ParentMaterialDtoResponse} from "@/model/be/jinear-core";
import {api} from "./api";

export const materialListingApi = api.injectEndpoints({
    endpoints: (build) => ({
        searchMaterial: build.query<ParentMaterialDtoResponse, MaterialSearchRequest>({
            query: (body) => {
                return {
                    url: `v1/material/list/search`,
                    method: "POST",
                    body
                };
            },
            providesTags: (_result, _err, materialSearchRequest) => [
                {
                    type: `v1/material/list/search`,
                    id: `${JSON.stringify(materialSearchRequest)}`
                }
            ]
        }),
        //
        // searchMaterialInfinite: build.infiniteQuery<
        //   ParentMaterialDtoResponse,
        //   Omit<MaterialSearchRequest, "page">,
        //   number
        // >({
        //   infiniteQueryOptions: {
        //     initialPageParam: 0,
        //     getNextPageParam: (lastPage, allPages, lastPageParam) => {
        //       return lastPage?.data?.content?.hasNext ? lastPageParam + 1 : undefined;
        //     }
        //   },
        //   query: ({ queryArg, pageParam }) => ({
        //     url: `v1/material/list/search`,
        //     method: "POST",
        //     body: { ...queryArg, page: pageParam }
        //   }),
        //   providesTags: (_result, _err, queryArg) => [
        //     {
        //       type: `v1/material/list/search`,
        //       id: `${JSON.stringify(queryArg)}`
        //     }
        //   ]
        // })
        //
    })
});

export const {useSearchMaterialQuery, useLazySearchMaterialQuery} = materialListingApi;

export const {
    endpoints: {searchMaterial}
} = materialListingApi;
