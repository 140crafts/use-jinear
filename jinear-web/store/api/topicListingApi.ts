import { TopicListingResponse } from "@/model/be/jinear-core";
import { api } from "./api";

export const topicListingApi = api.injectEndpoints({
  endpoints: (build) => ({
    retrieveTeamTopics: build.query<TopicListingResponse, string>({
      query: (teamId: string) => `v1/topic/list/${teamId}`,
      providesTags: (_result, _err, teamId) => [
        {
          type: "team-topic-list",
          id: teamId,
        },
      ],
    }),
  }),
});

export const { useRetrieveTeamTopicsQuery } = topicListingApi;

export const {
  endpoints: { retrieveTeamTopics },
} = topicListingApi;
