import type {NoteFilterRequest, NotePaginatedResponse} from "@/model/be/jinear-core";
import {api} from "./api";

export const noteFilterApi = api.injectEndpoints({
    endpoints: (build) => ({
        filterNotes: build.query<NotePaginatedResponse, NoteFilterRequest>({
            query: (req) => {
                return {
                    url: `v1/note/filter`,
                    method: "POST",
                    body: req
                };
            },

            providesTags: (_result, _err, req) => [
                {
                    type: "v1/note/filter",
                    id: `${req.page}-${req.workspaceId}-${req.notebookId}-${req.parentNoteId}-${req.noteId}`
                }
            ]
        })
    })
});

export const {useFilterNotesQuery} = noteFilterApi;

export const {
    endpoints: {filterNotes}
} = noteFilterApi;
