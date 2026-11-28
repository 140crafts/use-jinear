import type {NoteFilterRequest, NotePaginatedResponse, TaskLinkingNoteListResponse} from "@/model/be/jinear-core";
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
        }),
        //
        retrieveNotesLinkingTask: build.query<TaskLinkingNoteListResponse, string>({
            query: (taskId) => `v1/note/linking-task/${taskId}`,
            providesTags: (_result, _err, taskId) => [{type: "v1/note/linking-task/{taskId}", id: taskId}]
        })
    })
});

export const {useFilterNotesQuery, useRetrieveNotesLinkingTaskQuery} = noteFilterApi;

export const {
    endpoints: {filterNotes, retrieveNotesLinkingTask}
} = noteFilterApi;
