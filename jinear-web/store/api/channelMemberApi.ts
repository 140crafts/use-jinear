import { BaseResponse, ChannelMemberListingResponse } from "@/model/be/jinear-core";
import { api } from "./api";

export const channelMemberApi = api.injectEndpoints({
  endpoints: (build) => ({
    //
    retrieveChannelMemberships: build.query<ChannelMemberListingResponse, { workspaceId: string }>({
      query: (req: { workspaceId: string }) => `v1/messaging/channel/member/memberships/${req.workspaceId}`,
      providesTags: (_result, _err, req) => [
        {
          type: `v1/messaging/channel/member/memberships/{workspaceId}`,
          id: `${req.workspaceId}`
        }
      ]
    }),
    //
    retrieveChannelMembers: build.query<ChannelMemberListingResponse, { channelId: string }>({
      query: (req: { channelId: string }) => `v1/messaging/channel/member/list/${req.channelId}`,
      providesTags: (_result, _err, req) => [
        {
          type: `v1/messaging/channel/member/list/{channelId}`,
          id: `${req.channelId}`
        }
      ]
    }),
    //
    removeChannelMember: build.mutation<BaseResponse, { channelId: string; accountId: string }>({
      query: ({ channelId, accountId }: { channelId: string; accountId: string }) => ({
        url: `v1/messaging/channel/member/remove/${channelId}/account/${accountId}`,
        method: "POST"
      }),
      invalidatesTags: (_result, _err, req) => [
        `v1/messaging/channel/member/list/{channelId}`
      ]
    }),
    //
    addChannelMember: build.mutation<BaseResponse, { channelId: string; accountId: string }>({
      query: ({ channelId, accountId }: { channelId: string; accountId: string }) => ({
        url: `v1/messaging/channel/member/add/${channelId}/account/${accountId}`,
        method: "POST"
      }),
      invalidatesTags: (_result, _err, req) => [
        `v1/messaging/channel/member/list/{channelId}`
      ]
    })
    //
  })
});

export const {
  useRetrieveChannelMembershipsQuery,
  useRetrieveChannelMembersQuery,
  useRemoveChannelMemberMutation,
  useAddChannelMemberMutation
} = channelMemberApi;

export const {
  endpoints: { retrieveChannelMemberships, retrieveChannelMembers, removeChannelMember, addChannelMember }
} = channelMemberApi;
