import {
  BaseResponse, ConversationInitializeResponse,
  ConversationParticipantListingResponse,
  InitializeConversationRequest
} from "@/model/be/jinear-core";
import { api } from "./api";
import { addYears } from "date-fns";

export const conversationApi = api.injectEndpoints({
  endpoints: (build) => ({
    //
    initializeConversation: build.mutation<ConversationInitializeResponse, InitializeConversationRequest>({
      query: (req) => ({
        url: `v1/messaging/conversation`,
        method: "POST",
        body: req
      }),
      invalidatesTags: (_result, _err, req) => [
        `v1/messaging/conversation/participated/{workspaceId}`
      ]
    }),
    //
    retrieveParticipatedConversations: build.query<ConversationParticipantListingResponse, { workspaceId: string }>({
      query: (req: { workspaceId: string }) => `v1/messaging/conversation/participated/${req.workspaceId}`,
      providesTags: (_result, _err, req) => [
        {
          type: `v1/messaging/conversation/participated/{workspaceId}`,
          id: `${req.workspaceId}`
        }
      ]
    }),
    //
    muteConversation: build.mutation<BaseResponse, { conversationId: string, silentUntil: Date }>({
      query: ({ conversationId, silentUntil = addYears(new Date(), 1) }) => ({
        url: `v1/messaging/conversation/${conversationId}/mute?before=${silentUntil.toISOString()}`,
        method: "POST",
        body: { conversationId, silentUntil }
      }),
      invalidatesTags: (_result, _err, req) => [
        `v1/messaging/conversation/participated/{workspaceId}`
      ]
    })
  })
});

export const {
  useInitializeConversationMutation,
  useRetrieveParticipatedConversationsQuery,
  useLazyRetrieveParticipatedConversationsQuery,
  useMuteConversationMutation
} = conversationApi;

export const {
  endpoints: { initializeConversation, retrieveParticipatedConversations, muteConversation }
} = conversationApi;
