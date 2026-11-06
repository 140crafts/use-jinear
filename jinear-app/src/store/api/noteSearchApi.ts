import type {NotePaginatedResponse, NoteSearchRequest} from "@/model/be/jinear-core";
import {api} from "./api";

export const noteSearchApi = api.injectEndpoints({
    endpoints: (build) => ({
        searchNotes: build.query<NotePaginatedResponse, { workspaceId: string; body: NoteSearchRequest; page?: number }>({
            query: (req) => {
                const page = req.page ? req.page : 0;
                return {
                    url: `v1/note/workspace/${req.workspaceId}/search?page=${page}`,
                    method: "POST",
                    body: req.body
                };
            },
            providesTags: (_result, _err, req) => [
                {
                    type: "v1/note/workspace/{workspaceId}/search",
                    id: `${JSON.stringify(req)}`
                }
            ]
        })
        //
    })
});

export const {useSearchNotesQuery, useLazySearchNotesQuery} = noteSearchApi;

export const {
    endpoints: {searchNotes}
} = noteSearchApi;
