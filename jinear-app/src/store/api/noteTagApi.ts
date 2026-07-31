import type {
    AssignNoteTagRequest,
    BaseResponse,
    NoteTagInitializeRequest,
    NoteTagInitializeResponse,
    NoteTagListingResponse,
    NoteTagUpdateRequest,
    UpdateNoteTagAssignmentsRequest
} from "@/model/be/jinear-core";
import {api} from "./api";

export const noteTagApi = api.injectEndpoints({
    endpoints: (build) => ({
        listNoteTags: build.query<NoteTagListingResponse, { notebookId: string }>({
            query: ({notebookId}: { notebookId: string }) => `v1/note/tag/notebook/${notebookId}`,
            providesTags: (_result, _err, req) => [
                {type: "v1/note/tag/notebook/{notebookId}", id: req.notebookId}
            ]
        }),
        //
        initializeNoteTag: build.mutation<NoteTagInitializeResponse, NoteTagInitializeRequest>({
            query: (body: NoteTagInitializeRequest) => ({
                url: `v1/note/tag`,
                method: "POST",
                body
            }),
            invalidatesTags: ["v1/note/tag/notebook/{notebookId}"]
        }),
        //
        updateNoteTag: build.mutation<BaseResponse, NoteTagUpdateRequest>({
            query: (body: NoteTagUpdateRequest) => ({
                url: `v1/note/tag`,
                method: "PUT",
                body
            }),
            invalidatesTags: ["v1/note/tag/notebook/{notebookId}", "v1/note/notebook/{notebookId}/{noteId}"]
        }),
        //
        deleteNoteTag: build.mutation<BaseResponse, { noteTagId: string }>({
            query: (req: { noteTagId: string }) => ({
                url: `v1/note/tag/${req.noteTagId}`,
                method: "DELETE"
            }),
            invalidatesTags: ["v1/note/tag/notebook/{notebookId}", "v1/note/notebook/{notebookId}/{noteId}"]
        }),
        //
        assignNoteTag: build.mutation<BaseResponse, AssignNoteTagRequest>({
            query: (body: AssignNoteTagRequest) => ({
                url: `v1/note/tag/assign`,
                method: "POST",
                body
            }),
            invalidatesTags: ["v1/note/notebook/{notebookId}/{noteId}"]
        }),
        //
        updateNoteTagAssignments: build.mutation<BaseResponse, UpdateNoteTagAssignmentsRequest>({
            query: (body: UpdateNoteTagAssignmentsRequest) => ({
                url: `v1/note/tag/assignments`,
                method: "POST",
                body
            }),
            invalidatesTags: ["v1/note/notebook/{notebookId}/{noteId}", "v1/note/filter"]
        }),
        //
        unassignNoteTag: build.mutation<BaseResponse, AssignNoteTagRequest>({
            query: (body: AssignNoteTagRequest) => ({
                url: `v1/note/tag/unassign`,
                method: "POST",
                body
            }),
            invalidatesTags: ["v1/note/notebook/{notebookId}/{noteId}"]
        })
    })
});

export const {
    useListNoteTagsQuery,
    useInitializeNoteTagMutation,
    useUpdateNoteTagMutation,
    useDeleteNoteTagMutation,
    useAssignNoteTagMutation,
    useUpdateNoteTagAssignmentsMutation,
    useUnassignNoteTagMutation
} = noteTagApi;

export const {
    endpoints: {
        listNoteTags,
        initializeNoteTag,
        updateNoteTag,
        deleteNoteTag,
        assignNoteTag,
        updateNoteTagAssignments,
        unassignNoteTag
    }
} = noteTagApi;
