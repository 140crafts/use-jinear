import { RetrieveTopicListRequest, TopicListingResponse, TopicSearchResponse } from "@/model/be/jinear-core";
import { api } from "./api";

interface ISearchTeamTopics {
  teamId: string;
  nameOrTag: string;
}

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
    //
    retrieveExactTeamTopics: build.query<TopicSearchResponse, { teamId: string; body: RetrieveTopicListRequest }>({
      query: (req: { teamId: string; body: RetrieveTopicListRequest }) => ({
        url: `v1/topic/list/${req.teamId}/retrieve-exact`,
        method: "POST",
        body: req.body,
      }),
      providesTags: (_result, _err, req) => [
        {
          type: "team-topic-find-exact",
          id: `${JSON.stringify(req)}`,
        },
      ],
    }),
    //
    searchTeamTopics: build.query<TopicSearchResponse, ISearchTeamTopics>({
      query: ({ teamId, nameOrTag = "" }: ISearchTeamTopics) =>
        `v1/topic/list/${teamId}/search?nameOrTag=${encodeURI(nameOrTag)}`,
      providesTags: (_result, _err, req) => [
        {
          type: "team-topic-search",
          id: `${req.teamId}-${req.nameOrTag}`,
        },
      ],
    }),
  }),
});

export const { useRetrieveTeamTopicsQuery, useRetrieveExactTeamTopicsQuery, useSearchTeamTopicsQuery } = topicListingApi;

export const {
  endpoints: { retrieveTeamTopics, retrieveExactTeamTopics, searchTeamTopics },
} = topicListingApi;
