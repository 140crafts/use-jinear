import { BaseResponse, TopicInitializeRequest, TopicResponse, TopicUpdateRequest } from "@/model/be/jinear-core";
import { api } from "./api";

export const topicApi = api.injectEndpoints({
  endpoints: (build) => ({
    retrieveTopic: build.query<TopicResponse, string>({
      query: (topicId: string) => `v1/topic/${topicId}`,
      providesTags: (_result, _err, topicId) => [
        {
          type: "retrieve-topic",
          id: topicId,
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
      invalidatesTags: (_result, _err, req) => [{ type: "team-topic-list", id: req.teamId }, "team-topic-search"],
    }),
    //
    updateTopic: build.mutation<TopicResponse, TopicUpdateRequest>({
      query: (request: TopicUpdateRequest) => ({
        url: `v1/topic`,
        method: "PUT",
        body: request,
      }),
      invalidatesTags: ["team-topic-list", "retrieve-topic", "team-topic-search"],
    }),
    //
    deleteTopic: build.mutation<BaseResponse, string>({
      query: (topicId: string) => ({
        url: `v1/topic/${topicId}`,
        method: "DELETE",
      }),
      invalidatesTags: ["team-topic-list", "retrieve-topic", "team-topic-search"],
    }),
    //
  }),
});

export const { useRetrieveTopicQuery, useInitializeTopicMutation, useUpdateTopicMutation, useDeleteTopicMutation } = topicApi;

export const {
  endpoints: { retrieveTopic, initializeTopic, updateTopic, deleteTopic },
} = topicApi;
