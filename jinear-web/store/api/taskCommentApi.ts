import { BaseResponse, InitializeTaskCommentRequest, PaginatedTaskCommentResponse } from "@/model/be/jinear-core";
import { api } from "./api";

export const taskCommentApi = api.injectEndpoints({
  endpoints: (build) => ({
    initializeTaskComment: build.mutation<BaseResponse, InitializeTaskCommentRequest>({
      query: (body: InitializeTaskCommentRequest) => ({
        url: `v1/task/comment`,
        method: "POST",
        body,
      }),
      invalidatesTags: ["v1/task/comment/from-task/{taskId}", "v1/workspace/activity/filter"],
    }),
    //
    retrieveTaskComments: build.query<
      PaginatedTaskCommentResponse,
      {
        taskId: string;
        page?: number;
      }
    >({
      query: (req: { taskId: string; page?: number }) => `v1/task/comment/from-task/${req.taskId}?page=${req.page || "0"}`,
      providesTags: (_result, _err, req) => [
        {
          type: "v1/task/comment/from-task/{taskId}",
          id: `${req.taskId}-${req.page || "0"}`,
        },
      ],
    }),
    //
    deleteTaskComment: build.mutation<
      BaseResponse,
      {
        commentId: string;
      }
    >({
      query: (req) => ({
        url: `v1/task/comment/${req.commentId}`,
        method: "DELETE",
      }),
      invalidatesTags: (_result, _err, req) => ["v1/task/comment/from-task/{taskId}", "v1/workspace/activity/filter"],
    }),
    //
  }),
});

export const { useInitializeTaskCommentMutation, useRetrieveTaskCommentsQuery, useDeleteTaskCommentMutation } = taskCommentApi;

export const {
  endpoints: { initializeTaskComment, retrieveTaskComments, deleteTaskComment },
} = taskCommentApi;
