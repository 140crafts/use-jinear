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
        `v1/messaging/channel/member/list/{channelId}`,
        `v1/messaging/channel/workspace/{workspaceId}`
      ]
    }),
    //
    addChannelMember: build.mutation<BaseResponse, { channelId: string; accountId: string }>({
      query: ({ channelId, accountId }: { channelId: string; accountId: string }) => ({
        url: `v1/messaging/channel/member/add/${channelId}/account/${accountId}`,
        method: "POST"
      }),
      invalidatesTags: (_result, _err, req) => [
        `v1/messaging/channel/member/list/{channelId}`,
        `v1/messaging/channel/workspace/{workspaceId}`
      ]
    }),
    //
    joinChannel: build.mutation<BaseResponse, { channelId: string }>({
      query: ({ channelId }: { channelId: string; }) => ({
        url: `v1/messaging/channel/member/join/${channelId}`,
        method: "POST"
      }),
      invalidatesTags: (_result, _err, req) => [
        `v1/messaging/channel/member/list/{channelId}`,
        `v1/messaging/channel/member/memberships/{workspaceId}`,
        `v1/messaging/channel/workspace/{workspaceId}`
      ]
    }),
    //
    leaveChannel: build.mutation<BaseResponse, { channelId: string }>({
      query: ({ channelId }: { channelId: string }) => ({
        url: `v1/messaging/channel/member/leave/${channelId}`,
        method: "POST"
      }),
      invalidatesTags: (_result, _err, req) => [
        `v1/messaging/channel/member/list/{channelId}`,
        `v1/messaging/channel/member/memberships/{workspaceId}`,
        `v1/messaging/channel/workspace/{workspaceId}`
      ]
    }),
    //
    muteChannel: build.mutation<BaseResponse, { channelId: string }>({
      query: ({ channelId }: { channelId: string }) => ({
        url: `v1/messaging/channel/member/channel/${channelId}/mute`,
        method: "POST"
      }),
      invalidatesTags: (_result, _err, req) => [
        `v1/messaging/channel/member/memberships/{workspaceId}`,
        `v1/messaging/channel/workspace/{workspaceId}`
      ]
    }),
    //
    authorizeChannelMember: build.mutation<BaseResponse, { channelId: string, accountId: string }>({
      query: ({ channelId, accountId }: { channelId: string; accountId: string }) => ({
        url: `v1/messaging/channel/member/authorize/${channelId}/account/${accountId}`,
        method: "POST"
      }),
      invalidatesTags: (_result, _err, req) => [
        `v1/messaging/channel/member/memberships/{workspaceId}`,
        `v1/messaging/channel/member/list/{channelId}`,
        `v1/messaging/channel/workspace/{workspaceId}`
      ]
    }),
    //
    unAuthorizeChannelMember: build.mutation<BaseResponse, { channelId: string, accountId: string }>({
      query: ({ channelId, accountId }: { channelId: string; accountId: string }) => ({
        url: `v1/messaging/channel/member/un-authorize/${channelId}/account/${accountId}`,
        method: "POST"
      }),
      invalidatesTags: (_result, _err, req) => [
        `v1/messaging/channel/member/memberships/{workspaceId}`,
        `v1/messaging/channel/member/list/{channelId}`,
        `v1/messaging/channel/workspace/{workspaceId}`
      ]
    })
    //
  })
});

export const {
  useRetrieveChannelMembershipsQuery,
  useLazyRetrieveChannelMembershipsQuery,
  useRetrieveChannelMembersQuery,
  useRemoveChannelMemberMutation,
  useAddChannelMemberMutation,
  useJoinChannelMutation,
  useLeaveChannelMutation,
  useAuthorizeChannelMemberMutation,
  useUnAuthorizeChannelMemberMutation
} = channelMemberApi;

export const {
  endpoints: { retrieveChannelMemberships, retrieveChannelMembers, removeChannelMember, addChannelMember, joinChannel }
} = channelMemberApi;
