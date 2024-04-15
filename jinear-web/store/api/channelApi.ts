import { BaseResponse, ChannelMemberListingResponse, UpdateChannelRequest } from "@/model/be/jinear-core";
import { api } from "./api";

export const channelApi = api.injectEndpoints({
  endpoints: (build) => ({
    //
    updateChannelTitle: build.mutation<BaseResponse, { channelId: string, body: UpdateChannelRequest }>({
      query: ({ channelId, body }: { channelId: string, body: UpdateChannelRequest }) => ({
        url: `v1/messaging/channel/${channelId}/title`,
        method: "PUT",
        body: body
      }),
      invalidatesTags: (_result, _err, req) => [
        `v1/messaging/channel/member/{workspaceId}`
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
        `v1/messaging/channel/member/{workspaceId}`
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
        `v1/messaging/channel/member/{workspaceId}`
      ]
    })
    //
  })
});

export const {
  useUpdateChannelTitleMutation,
  useUpdateChannelVisibilityMutation,
  useUpdateChannelParticipationMutation
} = channelApi;

export const {
  endpoints: { updateChannelTitle, updateChannelVisibility, updateChannelParticipation }
} = channelApi;
