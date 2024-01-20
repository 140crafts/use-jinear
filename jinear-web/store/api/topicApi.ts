import { BaseResponse, TopicInitializeRequest, TopicResponse, TopicUpdateRequest } from "@/model/be/jinear-core";
import { api } from "./api";

export const topicApi = api.injectEndpoints({
  endpoints: (build) => ({
    retrieveTopic: build.query<TopicResponse, string>({
      query: (topicId: string) => `v1/topic/${topicId}`,
      providesTags: (_result, _err, topicId) => [
        {
          type: "v1/topic/{topicId}",
          id: topicId,
        },
      ],
    }),
    //
    retrieveTopicByTag: build.query<
      TopicResponse,
      {
        topicTag: string;
        workspaceId: string;
        teamId: string;
      }
    >({
      query: (req: { topicTag: string; workspaceId: string; teamId: string }) =>
        `v1/topic/tag/${req.topicTag}/workspace/${req.workspaceId}/team/${req.teamId}`,
      providesTags: (_result, _err, req: { topicTag: string; workspaceId: string; teamId: string }) => [
        {
          type: "v1/topic/tag/{topicTag}/workspace/{workspaceId}/team/{teamId}",
          id: `${req.topicTag}-${req.workspaceId}-${req.teamId}`,
        },
      ],
    }),
    //
    initializeTopic: build.mutation<TopicResponse, TopicInitializeRequest>({
      query: (request: TopicInitializeRequest) => ({
        url: `v1/topic`,
        method: "POST",
        body: request,
      }),
      invalidatesTags: (_result, _err, req) => [
        { type: "v1/topic/list/{teamId}", id: req.teamId },
        "v1/topic/list/{teamId}/search",
        "v1/workspace/activity/filter",
        "v1/topic/list/{teamId}/retrieve-exact",
      ],
    }),
    //
    updateTopic: build.mutation<TopicResponse, TopicUpdateRequest>({
      query: (request: TopicUpdateRequest) => ({
        url: `v1/topic`,
        method: "PUT",
        body: request,
      }),
      invalidatesTags: [
        "v1/topic/list/{teamId}",
        "v1/topic/{topicId}",
        "v1/topic/tag/{topicTag}/workspace/{workspaceId}/team/{teamId}",
        "v1/topic/list/{teamId}/search",
        "v1/topic/list/{teamId}/retrieve-exact",
      ],
    }),
    //
    deleteTopic: build.mutation<BaseResponse, string>({
      query: (topicId: string) => ({
        url: `v1/topic/${topicId}`,
        method: "DELETE",
      }),
      invalidatesTags: [
        "v1/topic/list/{teamId}",
        "v1/topic/{topicId}",
        "v1/topic/list/{teamId}/search",
        "v1/topic/tag/{topicTag}/workspace/{workspaceId}/team/{teamId}",
        "v1/workspace/activity/filter",
        "v1/topic/list/{teamId}/retrieve-exact",
      ],
    }),
    //
  }),
});

export const {
  useRetrieveTopicQuery,
  useRetrieveTopicByTagQuery,
  useInitializeTopicMutation,
  useUpdateTopicMutation,
  useDeleteTopicMutation,
} = topicApi;

export const {
  endpoints: { retrieveTopic, retrieveTopicByTag, initializeTopic, updateTopic, deleteTopic },
} = topicApi;
