import type {NotebookListingResponse, NotebookResponse} from "@/model/be/jinear-core";
import {api} from "./api";

interface IListWorkspaceNotebooksRequest {
    workspaceId: string;
    page?: number;
}

export const notebookListingApi = api.injectEndpoints({
    endpoints: (build) => ({
        getNotebook: build.query<NotebookResponse, { notebookId: string }>({
            query: ({notebookId}: { notebookId: string }) => `v1/notebook/${notebookId}`,
            providesTags: (_result, _err, req) => [
                {type: "v1/notebook/{notebookId}", id: req.notebookId}
            ]
        }),
        //
        listWorkspaceNotebooks: build.query<NotebookListingResponse, IListWorkspaceNotebooksRequest>({
            query: ({workspaceId, page = 0}: IListWorkspaceNotebooksRequest) =>
                `v1/notebook/workspace/${workspaceId}?page=${page}`,
            providesTags: (_result, _err, req) => [
                {type: "v1/notebook/workspace/{workspaceId}", id: `${req.workspaceId}-${req.page}`}
            ]
        })
    })
});

export const {useGetNotebookQuery, useListWorkspaceNotebooksQuery} = notebookListingApi;

export const {
    endpoints: {getNotebook, listWorkspaceNotebooks}
} = notebookListingApi;
