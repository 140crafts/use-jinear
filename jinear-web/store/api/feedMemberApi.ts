import { FeedMemberListingResponse } from "@/model/be/jinear-core";
import { api } from "./api";

export const feedMemberApi = api.injectEndpoints({
  endpoints: (build) => ({
    //
    retrieveFeedMemberships: build.query<FeedMemberListingResponse, { workspaceId: string }>({
      query: (req: { workspaceId: string }) => `v1/feed/member/memberships/${req.workspaceId}`,
      providesTags: (_result, _err, req) => [
        {
          type: "v1/feed/member/memberships/{workspaceId}",
          id: `${req.workspaceId}`,
        },
      ],
    }),
    //
  }),
});

export const { useRetrieveFeedMembershipsQuery } = feedMemberApi;

export const {
  endpoints: { retrieveFeedMemberships },
} = feedMemberApi;
