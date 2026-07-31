import type {BaseResponse, NotebookMemberPaginatedResponse} from "@/model/be/jinear-core";
import {api} from "./api";

interface IGetNotebookMembersRequest {
    notebookId: string;
    page?: number;
}

interface INotebookMemberAccountRequest {
    notebookId: string;
    accountId: string;
}

export const notebookMemberApi = api.injectEndpoints({
    endpoints: (build) => ({
        getNotebookMembers: build.query<NotebookMemberPaginatedResponse, IGetNotebookMembersRequest>({
            query: ({notebookId, page = 0}: IGetNotebookMembersRequest) =>
                `v1/notebook/member/${notebookId}?page=${page}`,
            providesTags: (_result, _err, req) => [
                {type: "v1/notebook/member/{notebookId}", id: `${req.notebookId}-${req.page}`}
            ]
        }),
        //
        addNotebookMember: build.mutation<BaseResponse, INotebookMemberAccountRequest>({
            query: (req: INotebookMemberAccountRequest) => ({
                url: `v1/notebook/member/${req.notebookId}/account/${req.accountId}`,
                method: "POST"
            }),
            invalidatesTags: ["v1/notebook/member/{notebookId}"]
        }),
        //
        removeNotebookMember: build.mutation<BaseResponse, INotebookMemberAccountRequest>({
            query: (req: INotebookMemberAccountRequest) => ({
                url: `v1/notebook/member/${req.notebookId}/account/${req.accountId}`,
                method: "DELETE"
            }),
            invalidatesTags: ["v1/notebook/member/{notebookId}"]
        })
    })
});

export const {
    useGetNotebookMembersQuery,
    useAddNotebookMemberMutation,
    useRemoveNotebookMemberMutation
} = notebookMemberApi;

export const {
    endpoints: {getNotebookMembers, addNotebookMember, removeNotebookMember}
} = notebookMemberApi;
