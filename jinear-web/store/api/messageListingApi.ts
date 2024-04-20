import { api } from "./api";
import { MessageListingPaginatedResponse } from "@/be/jinear-core";
import { addSeconds } from "date-fns";

export const messageListingApi = api.injectEndpoints({
  endpoints: (build) => ({
    //
    retrieveThreadMessages: build.query<MessageListingPaginatedResponse, { threadId: string, before?: Date }>({
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
    })
    //
  })
});

export const { useRetrieveThreadMessagesQuery, useLazyRetrieveThreadMessagesQuery } = messageListingApi;

export const {
  endpoints: { retrieveThreadMessages }
} = messageListingApi;
