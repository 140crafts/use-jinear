import type {
    BaseResponse,
    NoteInitializeRequest,
    NoteInitializeResponse,
    NoteMoveRequest,
    NoteUpdateRequest
} from "@/model/be/jinear-core";
import {api} from "./api";
import {noteFilterApi} from "./noteFilterApi";
import type {RootState} from "@/store";

const OPERATION_TAG = ["v1/note/notebook/{notebookId}/{noteId}", "v1/note/filter"];

export const noteOperationApi = api.injectEndpoints({
    endpoints: (build) => ({
        initializeNote: build.mutation<NoteInitializeResponse, { workspaceId: string } & NoteInitializeRequest>({
            query: ({workspaceId, ...body}) => ({
                url: `v1/note/workspace/${workspaceId}/operation`,
                method: "POST",
                body
            }),
            invalidatesTags: OPERATION_TAG
        }),
        //
        updateNote: build.mutation<BaseResponse, { workspaceId: string } & NoteUpdateRequest>({
            query: ({workspaceId, ...body}) => ({
                url: `v1/note/workspace/${workspaceId}/operation`,
                method: "PUT",
                body
            }),
            invalidatesTags: OPERATION_TAG
        }),
        //
        changeNotebook: build.mutation<BaseResponse, { workspaceId: string; noteId: string; notebookId: string }>({
            query: ({workspaceId, noteId, notebookId}) => ({
                url: `v1/note/workspace/${workspaceId}/operation/${noteId}/change-notebook/${notebookId}`,
                method: "PUT"
            }),
            invalidatesTags: OPERATION_TAG
        }),
        //
        moveNote: build.mutation<BaseResponse, { workspaceId: string } & NoteMoveRequest>({
            query: ({workspaceId, ...body}) => ({
                url: `v1/note/workspace/${workspaceId}/operation/move`,
                method: "PUT",
                body
            }),
            invalidatesTags: OPERATION_TAG
        }),
        //
        deleteNote: build.mutation<BaseResponse, { workspaceId: string; noteId: string }>({
            query: ({workspaceId, noteId}) => ({
                url: `v1/note/workspace/${workspaceId}/operation/${noteId}`,
                method: "DELETE"
            }),
            invalidatesTags: OPERATION_TAG,
            /**
             * Invalidation only schedules refetches — until they land, every cached list still contains the
             * deleted note. NotebookFirstNoteNavigator reads those lists to decide where to redirect, so a
             * stale entry sends the user straight back into the note they just deleted. Rewriting the cache
             * makes the deletion true for every reader at once (the sidebar included), and the refetch that
             * follows just confirms it.
             */
            async onQueryStarted({noteId}, {dispatch, getState, queryFulfilled}) {
                // Pessimistic: a destructive change only enters the cache once the server confirms it.
                await queryFulfilled;
                const state = getState() as RootState;
                noteFilterApi.util.selectCachedArgsForQuery(state, "filterNotes")
                    // Single-note entries belong to the open editor; leaving those to invalidation keeps it
                    // from flashing "note not found" on its way out. Lists are what the navigator reads.
                    .filter(args => !args.noteId)
                    .forEach(args => dispatch(noteFilterApi.util.updateQueryData("filterNotes", args, draft => {
                        const content = draft?.data?.content;
                        const index = content?.findIndex(note => note.noteId === noteId) ?? -1;
                        if (index >= 0) content.splice(index, 1);
                    })));
            }
        })
    })
});

export const {
    useInitializeNoteMutation,
    useUpdateNoteMutation,
    useChangeNotebookMutation,
    useMoveNoteMutation,
    useDeleteNoteMutation
} = noteOperationApi;

export const {
    endpoints: {initializeNote, updateNote, changeNotebook, moveNote, deleteNote}
} = noteOperationApi;
