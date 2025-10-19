import { MessageResponse, SendMessageRequest } from "@/model/be/jinear-core";
import { api } from "./api";

export const messageOperationApi = api.injectEndpoints({
  endpoints: (build) => ({
    //
    sendToThread: build.mutation<MessageResponse, { workspaceId: string, channelId: string, threadId: string, body: SendMessageRequest }>({
      query: (req) => ({
        url: `v1/messaging/message/operation/thread/${req.threadId}/send`,
        method: "POST",
        body: req.body
      }),
      async onQueryStarted(props, { dispatch, queryFulfilled }) {
        await queryFulfilled;
        setTimeout(() => {
          dispatch(api.util.invalidateTags([
            { type: "v1/messaging/message/thread/{threadId}", id: props.threadId },
            { type: "v1/messaging/thread/{threadId}", id: props.threadId }
          ]));
        }, 3000);
      }
    }),
    //
    sendToConversation: build.mutation<MessageResponse, {
      workspaceId: string,
      conversationId: string,
      body: SendMessageRequest
    }>({
      query: (req) => ({
        url: `v1/messaging/message/operation/conversation/${req.conversationId}/send`,
        method: "POST",
        body: req.body
      }),
      async onQueryStarted(props, { dispatch, queryFulfilled }) {
        await queryFulfilled;
        setTimeout(() => {
          dispatch(api.util.invalidateTags([
            { type: "v1/messaging/message/conversation/{conversationId}", id: props.conversationId }
          ]));
        }, 3000);
      }
    })
    //
  })
});

export const {
  useSendToThreadMutation,
  useSendToConversationMutation
} = messageOperationApi;

export const {
  endpoints: { sendToThread, sendToConversation }
} = messageOperationApi;
