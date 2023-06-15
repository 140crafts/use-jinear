import { TopicListingResponse, TopicSearchResponse } from "@/model/be/jinear-core";
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

export const { useRetrieveTeamTopicsQuery, useSearchTeamTopicsQuery } = topicListingApi;

export const {
  endpoints: { retrieveTeamTopics, searchTeamTopics },
} = topicListingApi;
