import { BaseResponse } from "@/model/be/jinear-core";
import { api } from "./api";

export interface IProjectPostInitializeRequest {
  formData?: FormData;
}

export const projectPostApi = api.injectEndpoints({
  endpoints: (build) => ({
    //
    initializeProjectFeedPost: build.mutation<BaseResponse, IProjectPostInitializeRequest>({
      query: (request: IProjectPostInitializeRequest) => ({
        url: "v1/project/post",
        method: "POST",
        body: request.formData
      }),
      invalidatesTags: [`v1/project/public-feed/{projectId}/{postId}`, `v1/project/public-feed/{projectId}`]
    }),
    //
    deleteProjectFeedPost: build.mutation<BaseResponse, { projectId: string, postId: string }>({
      query: ({ projectId, postId }: { projectId: string, postId: string }) => ({
        url: `v1/project/post/${projectId}/${postId}`,
        method: "DELETE"
      }),
      invalidatesTags: [`v1/project/public-feed/{projectId}/{postId}`, `v1/project/public-feed/{projectId}`]
    }),
    //
    addMediaToProjectFeedPost: build.mutation<BaseResponse, {
      projectId: string,
      postId: string,
      formData?: FormData
    }>({
      query: ({ projectId, postId, formData }: { projectId: string, postId: string, formData?: FormData }) => ({
        url: `v1/project/post/${projectId}/${postId}/media`,
        method: "POST",
        body: formData
      }),
      invalidatesTags: [`v1/project/public-feed/{projectId}/{postId}`, `v1/project/public-feed/{projectId}`]
    }),
    //
    removeMediaFromProjectFeedPost: build.mutation<BaseResponse, {
      projectId: string,
      postId: string,
      mediaId: string
    }>({
      query: ({ projectId, postId, mediaId }: { projectId: string, postId: string, mediaId: string }) => ({
        url: `v1/project/post/${projectId}/${postId}/media/${mediaId}`,
        method: "DELETE"
      }),
      invalidatesTags: [`v1/project/public-feed/{projectId}/{postId}`, `v1/project/public-feed/{projectId}`]
    })
  })
});

export const {
  useInitializeProjectFeedPostMutation,
  useAddMediaToProjectFeedPostMutation,
  useDeleteProjectFeedPostMutation,
  useRemoveMediaFromProjectFeedPostMutation
} = projectPostApi;

export const {
  endpoints: {
    initializeProjectFeedPost,
    deleteProjectFeedPost,
    addMediaToProjectFeedPost,
    removeMediaFromProjectFeedPost
  }
} = projectPostApi;
