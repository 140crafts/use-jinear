import {
  BaseResponse, ChannelListingResponse,
  ChannelMemberListingResponse, ChannelMembershipInfoListingResponse,
  InitializeChannelRequest,
  UpdateChannelRequest
} from "@/model/be/jinear-core";
import { api } from "./api";

export const channelApi = api.injectEndpoints({
  endpoints: (build) => ({
    //
    initializeChannel: build.mutation<BaseResponse, InitializeChannelRequest>({
      query: (req) => ({
        url: `v1/messaging/channel`,
        method: "POST",
        body: req
      }),
      invalidatesTags: (_result, _err, req) => [
        `v1/messaging/channel/member/memberships/{workspaceId}`
      ]
    }),
    //
    updateChannelTitle: build.mutation<BaseResponse, { channelId: string, body: UpdateChannelRequest }>({
      query: ({ channelId, body }: { channelId: string, body: UpdateChannelRequest }) => ({
        url: `v1/messaging/channel/${channelId}/title`,
        method: "PUT",
        body: body
      }),
      invalidatesTags: (_result, _err, req) => [
        `v1/messaging/channel/member/memberships/{workspaceId}`
      ]
    }),
    //
    updateChannelVisibility: build.mutation<BaseResponse, { channelId: string, body: UpdateChannelRequest }>({
      query: ({ channelId, body }: { channelId: string, body: UpdateChannelRequest }) => ({
        url: `v1/messaging/channel/${channelId}/visibility`,
        method: "PUT",
        body: body
      }),
      invalidatesTags: (_result, _err, req) => [
        `v1/messaging/channel/member/memberships/{workspaceId}`
      ]
    }),
    //
    updateChannelParticipation: build.mutation<BaseResponse, { channelId: string, body: UpdateChannelRequest }>({
      query: ({ channelId, body }: { channelId: string, body: UpdateChannelRequest }) => ({
        url: `v1/messaging/channel/${channelId}/participation`,
        method: "PUT",
        body: body
      }),
      invalidatesTags: (_result, _err, req) => [
        `v1/messaging/channel/member/memberships/{workspaceId}`
      ]
    }),
    //
    listChannels: build.query<ChannelMembershipInfoListingResponse, { workspaceId: string }>({
      query: (req: { workspaceId: string }) => `v1/messaging/channel/workspace/${req.workspaceId}`,
      providesTags: (_result, _err, req) => [
        {
          type: `v1/messaging/channel/workspace/{workspaceId}`,
          id: `${req.workspaceId}`
        }
      ]
    })
    //
  })
});

export const {
  useInitializeChannelMutation,
  useUpdateChannelTitleMutation,
  useUpdateChannelVisibilityMutation,
  useUpdateChannelParticipationMutation,
  useListChannelsQuery
} = channelApi;

export const {
  endpoints: {
    initializeChannel,
    updateChannelTitle,
    updateChannelVisibility,
    updateChannelParticipation,
    listChannels
  }
} = channelApi;
