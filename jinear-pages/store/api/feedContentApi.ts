import { FeedContentItemResponse, FeedContentResponse } from "@/model/be/jinear-core";
import { api } from "./api";

export const feedContentApi = api.injectEndpoints({
  endpoints: (build) => ({
    //
    retrieveFeedContent: build.query<FeedContentResponse, { workspaceId: string; feedId: string; pageToken?: string }>({
      query: (req: { workspaceId: string; feedId: string; pageToken?: string }) =>
        `v1/feed/content/workspace/${req.workspaceId}/${req.feedId}` + (req.pageToken ? `?pageToken=${req.pageToken}` : ""),
      providesTags: (_result, _err, req) => [
        {
          type: "v1/feed/content/workspace/{workspaceId}/{feedId}",
          id: `${req.workspaceId}+${req.feedId}+${req.pageToken}`,
        },
      ],
    }),
    //
    retrieveFeedContentItem: build.query<FeedContentItemResponse, { workspaceId: string; feedId: string; itemId: string }>({
      query: (req: { workspaceId: string; feedId: string; itemId: string }) =>
        `v1/feed/content/workspace/${req.workspaceId}/${req.feedId}/item/${req.itemId}`,
      providesTags: (_result, _err, req) => [
        {
          type: "v1/feed/content/workspace/{workspaceId}/{feedId}/item/{itemId}",
          id: `${req.workspaceId}+${req.feedId}+${req.itemId}`,
        },
      ],
    }),
    //
  }),
});

export const { useRetrieveFeedContentQuery, useLazyRetrieveFeedContentQuery, useRetrieveFeedContentItemQuery } = feedContentApi;

export const {
  endpoints: { retrieveFeedContent, retrieveFeedContentItem },
} = feedContentApi;
