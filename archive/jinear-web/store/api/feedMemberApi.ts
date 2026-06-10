import { BaseResponse, FeedMemberListingResponse, FeedMemberPaginatedResponse } from "@/model/be/jinear-core";
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
    retrieveFeedMembers: build.query<FeedMemberPaginatedResponse, { feedId: string; page?: number }>({
      query: ({ feedId, page = 0 }: { feedId: string; page?: number }) => `v1/feed/member/list/${feedId}?page=${page}`,
      providesTags: (_result, _err, req) => [
        {
          type: "v1/feed/member/list/{feedId}",
          id: `${req.feedId}-${req.page}`,
        },
      ],
    }),
    //
    addFeedMember: build.mutation<BaseResponse, { feedId: string; accountId: string }>({
      query: ({ feedId, accountId }: { feedId: string; accountId: string }) => ({
        url: `v1/feed/member/${feedId}/manage/${accountId}`,
        method: "POST",
      }),
      invalidatesTags: (_result, _err, req) => ["v1/feed/member/list/{feedId}", "v1/feed/member/memberships/{workspaceId}"],
    }),
    //
    kickFeedMember: build.mutation<BaseResponse, { feedId: string; accountId: string }>({
      query: ({ feedId, accountId }: { feedId: string; accountId: string }) => ({
        url: `v1/feed/member/${feedId}/manage/${accountId}`,
        method: "DELETE",
      }),
      invalidatesTags: (_result, _err, req) => ["v1/feed/member/list/{feedId}", "v1/feed/member/memberships/{workspaceId}"],
    }),
    //
  }),
});

export const {
  useRetrieveFeedMembershipsQuery,
  useRetrieveFeedMembersQuery,
  useAddFeedMemberMutation,
  useKickFeedMemberMutation,
} = feedMemberApi;

export const {
  endpoints: { retrieveFeedMemberships, retrieveFeedMembers, addFeedMember, kickFeedMember },
} = feedMemberApi;
