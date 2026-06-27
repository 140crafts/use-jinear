import type {
    RichTextAppendDto,
    RichTextAppendResponse,
    RichTextStateDto,
    RichTextStateResponse,
    RichTextUpdatesDto,
    RichTextUpdatesResponse
} from "@/model/be/jinear-core";
import {api} from "./api";

/**
 * Transport for the backend CRDT relay (`/v1/rich-text/{id}/*`). The backend never decodes Yjs;
 * it is an opaque blob store + ordered relay. Every response is wrapped in `BaseResponse`, so each
 * endpoint unwraps `data` via `transformResponse`. Most of the sync loop (debounced push, polling)
 * is imperative, so the raw endpoints are re-exported for `store.dispatch(...endpoints.x.initiate())`.
 */
export const richTextSyncApi = api.injectEndpoints({
    endpoints: (build) => ({
        //
        getRichTextState: build.query<RichTextStateDto, { richTextId: string }>({
            query: ({richTextId}) => `v1/rich-text/${richTextId}/state`,
            transformResponse: (response: RichTextStateResponse) => response.data
        }),
        //
        getRichTextUpdates: build.query<RichTextUpdatesDto, { richTextId: string; since: number }>({
            query: ({richTextId, since}) => `v1/rich-text/${richTextId}/updates?since=${since}`,
            transformResponse: (response: RichTextUpdatesResponse) => response.data
        }),
        //
        appendRichTextUpdate: build.mutation<RichTextAppendDto, {
            richTextId: string;
            update: string;
            html?: string | null;
        }>({
            query: ({richTextId, update, html}) => ({
                url: `v1/rich-text/${richTextId}/updates`,
                method: "POST",
                body: {update, html}
            }),
            transformResponse: (response: RichTextAppendResponse) => response.data
        }),
        //
        seedRichText: build.mutation<RichTextStateDto, { richTextId: string; state: string }>({
            query: ({richTextId, state}) => ({
                url: `v1/rich-text/${richTextId}/seed`,
                method: "POST",
                body: {state}
            }),
            transformResponse: (response: RichTextStateResponse) => response.data
        }),
        //
        snapshotRichText: build.mutation<void, { richTextId: string; state: string; upToSeq: number }>({
            query: ({richTextId, state, upToSeq}) => ({
                url: `v1/rich-text/${richTextId}/snapshot`,
                method: "POST",
                body: {state, upToSeq}
            })
        })
        //
    })
});

export const {
    useGetRichTextStateQuery,
    useLazyGetRichTextStateQuery,
    useGetRichTextUpdatesQuery,
    useLazyGetRichTextUpdatesQuery,
    useAppendRichTextUpdateMutation,
    useSeedRichTextMutation,
    useSnapshotRichTextMutation
} = richTextSyncApi;

export const {
    endpoints: {
        getRichTextState,
        getRichTextUpdates,
        appendRichTextUpdate,
        seedRichText,
        snapshotRichText
    }
} = richTextSyncApi;
