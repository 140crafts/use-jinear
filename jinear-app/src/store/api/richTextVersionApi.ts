import type {
    PageDto,
    RichTextVersionDetailDto,
    RichTextVersionDto,
    RichTextVersionPaginatedResponse,
    RichTextVersionResponse
} from "@/model/be/jinear-core";
import {api} from "./api";

export const richTextVersionApi = api.injectEndpoints({
    endpoints: (build) => ({
        //
        retrieveRichTextVersions: build.infiniteQuery<PageDto<RichTextVersionDto>, { richTextId: string }, number>({
            infiniteQueryOptions: {
                initialPageParam: 0,
                getNextPageParam: (lastPage) => lastPage.hasNext ? lastPage.number + 1 : undefined
            },
            query: ({queryArg, pageParam}) => `v1/rich-text/${queryArg.richTextId}/versions?page=${pageParam}`,
            transformResponse: (response: RichTextVersionPaginatedResponse) => response.data,
            keepUnusedDataFor: 0
        }),
        //
        retrieveRichTextVersion: build.query<RichTextVersionDetailDto, { richTextId: string; richTextVersionId: string }>({
            query: ({richTextId, richTextVersionId}) => `v1/rich-text/${richTextId}/versions/${richTextVersionId}`,
            transformResponse: (response: RichTextVersionResponse) => response.data
        })
        //
    })
});

export const {
    useRetrieveRichTextVersionsInfiniteQuery,
    useRetrieveRichTextVersionQuery
} = richTextVersionApi;
