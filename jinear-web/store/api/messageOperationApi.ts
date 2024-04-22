import { MessageResponse, SendMessageRequest } from "@/model/be/jinear-core";
import { api } from "./api";

export const messageOperationApi = api.injectEndpoints({
  endpoints: (build) => ({
    //
    sendToThread: build.mutation<MessageResponse, { threadId: string, body: SendMessageRequest }>({
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
    })
    //
  })
});

export const {
  useSendToThreadMutation
} = messageOperationApi;

export const {
  endpoints: { sendToThread }
} = messageOperationApi;
