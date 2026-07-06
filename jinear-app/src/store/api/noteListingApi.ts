import type {NotePathAwareResponse} from "@/model/be/jinear-core";
import {api} from "./api";

interface IRetrieveNotePathAwareRequest {
    notebookId: string;
    noteId?: string;
    page?: number;
}

export const noteListingApi = api.injectEndpoints({
    endpoints: (build) => ({
        retrieveNotePathAware: build.query<NotePathAwareResponse, IRetrieveNotePathAwareRequest>({
            query: ({notebookId, noteId, page = 0}: IRetrieveNotePathAwareRequest) => {
                const params = new URLSearchParams({page: String(page)});
                if (noteId) params.set("noteId", noteId);
                return `v1/note/notebook/${notebookId}?${params}`;
            },
            providesTags: (_result, _err, req) => [
                {
                    type: "v1/note/notebook/{notebookId}/{noteId}",
                    id: `${req.notebookId}-${req.noteId}-${req.page}`
                }
            ]
        })
    })
});

export const {useRetrieveNotePathAwareQuery} = noteListingApi;

export const {
    endpoints: {retrieveNotePathAware}
} = noteListingApi;
