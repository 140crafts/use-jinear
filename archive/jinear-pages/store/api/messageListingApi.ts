import { api } from "./api";
import { MessageListingPaginatedResponse } from "@/be/jinear-core";
import { addSeconds } from "date-fns";

export const messageListingApi = api.injectEndpoints({
  endpoints: (build) => ({
    //
    retrieveThreadMessages: build.query<MessageListingPaginatedResponse, {
      workspaceId: string,
      channelId:string,
      threadId: string,
      before?: Date
    }>({
      query: ({
                threadId,
                before = addSeconds(new Date(), +10)
              }) => `v1/messaging/message/thread/${threadId}?before=${before.toISOString()}`,
      providesTags: (_result, _err, req) => [
        {
          type: `v1/messaging/message/thread/{threadId}`,
          id: `${req.threadId}-${req.before?.toISOString()}`
        },
        {
          type: `v1/messaging/message/thread/{threadId}`,
          id: `${req.threadId}`
        }
      ]
    }),
    //
    retrieveConversationMessages: build.query<MessageListingPaginatedResponse, {
      workspaceId: string,
      conversationId: string,
      before?: Date
    }>({
      query: ({
                conversationId,
                before = addSeconds(new Date(), +10)
              }) => `v1/messaging/message/conversation/${conversationId}?before=${before.toISOString()}`,
      providesTags: (_result, _err, req) => [
        {
          type: `v1/messaging/message/conversation/{conversationId}`,
          id: `${req.conversationId}-${req.before?.toISOString()}`
        },
        {
          type: `v1/messaging/message/conversation/{conversationId}`,
          id: `${req.conversationId}`
        }
      ]
    })
    //
  })
});

export const {
  useRetrieveThreadMessagesQuery,
  useLazyRetrieveThreadMessagesQuery,
  useRetrieveConversationMessagesQuery,
  useLazyRetrieveConversationMessagesQuery
} = messageListingApi;

export const {
  endpoints: { retrieveThreadMessages, retrieveConversationMessages }
} = messageListingApi;
