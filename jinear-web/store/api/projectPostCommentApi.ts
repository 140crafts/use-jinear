import {
  AccountProjectPermissionFlagsResponse, BaseResponse, ProjectFeedPaginatedResponse, ProjectFeedPostResponse,
  ProjectListingPaginatedResponse, ProjectPostAddCommentRequest, ProjectPostPaginatedCommentResponse,
  ProjectRetrieveResponse, PublicProjectRetrieveResponse
} from "@/model/be/jinear-core";
import { api } from "./api";

export const projectPostCommentApi = api.injectEndpoints({
  endpoints: (build) => ({
    //
    listProjectPostComments: build.query<ProjectPostPaginatedCommentResponse, {
      projectId: string,
      postId: string,
      page?: number
    }>({
      query: ({ projectId, postId, page = 0 }: {
        projectId: string,
        postId: string,
        page?: number
      }) => `v1/project/post/comment/list/project/${projectId}/post/${postId}?page=${page}`,
      providesTags: (_result, _err, req) => [
        {
          type: `v1/project/post/comment/list/project/{projectId}/post/{postId}`,
          id: `${req.projectId}-${req.postId}-${req.page}`
        }
      ]
    }),
    //
    addPostComment: build.mutation<BaseResponse, ProjectPostAddCommentRequest>({
      query: (req) => ({
        url: `v1/project/post/comment`,
        method: "POST",
        body: req
      }),
      invalidatesTags: [`v1/project/post/comment/list/project/{projectId}/post/{postId}`]
    }),
    //
    deletePostComment: build.mutation<BaseResponse, { commentId: string }>({
      query: ({ commentId }: { commentId: string }) => ({
        url: `v1/project/post/comment/${commentId}`,
        method: "DELETE"
      }),
      invalidatesTags: [`v1/project/post/comment/list/project/{projectId}/post/{postId}`]
    })
    //
    //
  })
});

export const {
  useListProjectPostCommentsQuery,
  useAddPostCommentMutation,
  useDeletePostCommentMutation
} = projectPostCommentApi;

export const {
  endpoints: {
    listProjectPostComments,
    addPostComment,
    deletePostComment
  }
} = projectPostCommentApi;
