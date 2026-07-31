import type {
    BaseResponse,
    NotebookInitializeRequest,
    NotebookInitializeResponse,
    NotebookUpdateRequest
} from "@/model/be/jinear-core";
import {api} from "./api";

export const notebookOperationApi = api.injectEndpoints({
    endpoints: (build) => ({
        initializeNotebook: build.mutation<NotebookInitializeResponse, {
            workspaceId: string;
            body: NotebookInitializeRequest;
        }>({
            query: (req: { workspaceId: string; body: NotebookInitializeRequest }) => ({
                url: `v1/notebook/operation/workspace/${req.workspaceId}`,
                method: "POST",
                body: req.body
            }),
            invalidatesTags: ["v1/notebook/workspace/{workspaceId}"]
        }),
        //
        updateNotebook: build.mutation<BaseResponse, NotebookUpdateRequest>({
            query: (body: NotebookUpdateRequest) => ({
                url: `v1/notebook/operation`,
                method: "PUT",
                body
            }),
            invalidatesTags: ["v1/notebook/{notebookId}", "v1/notebook/workspace/{workspaceId}"]
        }),
        //
        deleteNotebook: build.mutation<BaseResponse, { notebookId: string }>({
            query: (req: { notebookId: string }) => ({
                url: `v1/notebook/operation/${req.notebookId}`,
                method: "DELETE"
            }),
            invalidatesTags: ["v1/notebook/{notebookId}", "v1/notebook/workspace/{workspaceId}"]
        })
    })
});

export const {
    useInitializeNotebookMutation,
    useUpdateNotebookMutation,
    useDeleteNotebookMutation
} = notebookOperationApi;

export const {
    endpoints: {initializeNotebook, updateNotebook, deleteNotebook}
} = notebookOperationApi;
