import { ChannelMemberListingResponse } from "@/model/be/jinear-core";
import { api } from "./api";

export const channelMemberApi = api.injectEndpoints({
  endpoints: (build) => ({
    //
    retrieveMemberships: build.query<ChannelMemberListingResponse, { workspaceId: string }>({
      query: (req: { workspaceId: string }) => `v1/messaging/channel/member/memberships/${req.workspaceId}`,
      providesTags: (_result, _err, req) => [
        {
          type: `v1/messaging/channel/member/{workspaceId}`,
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
    })
    //
  })
});

export const {
  useRetrieveMembershipsQuery,
  useRetrieveChannelMembersQuery
} = channelMemberApi;

export const {
  endpoints: { retrieveMemberships, retrieveChannelMembers }
} = channelMemberApi;
