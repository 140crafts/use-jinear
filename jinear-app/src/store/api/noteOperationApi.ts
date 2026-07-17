import type {
    BaseResponse,
    NoteInitializeRequest,
    NoteInitializeResponse,
    NoteMoveRequest,
    NoteUpdateRequest
} from "@/model/be/jinear-core";
import {api} from "./api";

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
        publishNote: build.mutation<BaseResponse, { workspaceId: string; noteId: string; notebookId: string }>({
            query: ({workspaceId, noteId, notebookId}) => ({
                url: `v1/note/workspace/${workspaceId}/operation/${noteId}/publish/${notebookId}`,
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
            invalidatesTags: OPERATION_TAG
        })
    })
});

export const {
    useInitializeNoteMutation,
    useUpdateNoteMutation,
    usePublishNoteMutation,
    useMoveNoteMutation,
    useDeleteNoteMutation
} = noteOperationApi;

export const {
    endpoints: {initializeNote, updateNote, publishNote, moveNote, deleteNote}
} = noteOperationApi;
