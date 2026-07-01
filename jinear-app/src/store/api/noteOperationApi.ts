import type {
    BaseResponse,
    NoteInitializeRequest,
    NoteInitializeResponse,
    NoteMoveRequest,
    NoteUpdateRequest
} from "@/model/be/jinear-core";
import {api} from "./api";

export const noteOperationApi = api.injectEndpoints({
    endpoints: (build) => ({
        initializeNote: build.mutation<NoteInitializeResponse, NoteInitializeRequest>({
            query: (body: NoteInitializeRequest) => ({
                url: `v1/note/operation`,
                method: "POST",
                body
            }),
            invalidatesTags: ["v1/note/notebook/{notebookId}/{noteId}"]
        }),
        //
        updateNote: build.mutation<BaseResponse, NoteUpdateRequest>({
            query: (body: NoteUpdateRequest) => ({
                url: `v1/note/operation`,
                method: "PUT",
                body
            }),
            invalidatesTags: ["v1/note/notebook/{notebookId}/{noteId}"]
        }),
        //
        moveNote: build.mutation<BaseResponse, NoteMoveRequest>({
            query: (body: NoteMoveRequest) => ({
                url: `v1/note/operation/move`,
                method: "PUT",
                body
            }),
            invalidatesTags: ["v1/note/notebook/{notebookId}/{noteId}"]
        }),
        //
        deleteNote: build.mutation<BaseResponse, { noteId: string }>({
            query: (req: { noteId: string }) => ({
                url: `v1/note/operation/${req.noteId}`,
                method: "DELETE"
            }),
            invalidatesTags: ["v1/note/notebook/{notebookId}/{noteId}"]
        })
    })
});

export const {
    useInitializeNoteMutation,
    useUpdateNoteMutation,
    useMoveNoteMutation,
    useDeleteNoteMutation
} = noteOperationApi;

export const {
    endpoints: {initializeNote, updateNote, moveNote, deleteNote}
} = noteOperationApi;
