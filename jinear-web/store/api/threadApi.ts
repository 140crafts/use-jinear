import { BaseResponse, InitializeThreadRequest, ThreadListingResponse, ThreadResponse } from "@/model/be/jinear-core";
import { api } from "./api";
import { addSeconds } from "date-fns";

export const threadApi = api.injectEndpoints({
  endpoints: (build) => ({
    //
    initializeThread: build.mutation<BaseResponse, InitializeThreadRequest>({
      query: (req) => ({
        url: `v1/messaging/thread`,
        method: "POST",
        body: req
      }),
      async onQueryStarted(props, { dispatch, queryFulfilled }) {
        await queryFulfilled;
        setTimeout(() => {
          dispatch(api.util.invalidateTags([`v1/messaging/thread/channel/{channelId}`]));
        }, 1500);
      }
      // invalidatesTags: (_result, _err, req) => [
      //   // `v1/messaging/thread/channel/{channelId}`
      // ]
    }),
    //
    listThreads: build.query<ThreadListingResponse, { channelId: string, before?: Date }>({
      query: ({ channelId, before = addSeconds(new Date(), +10) }: {
        channelId: string,
        before?: Date
      }) => `v1/messaging/thread/channel/${channelId}?before=${before.toISOString()}`,
      providesTags: (_result, _err, req) => [
        {
          type: `v1/messaging/thread/channel/{channelId}`,
          id: `${req.channelId}-${req.before?.toISOString()}`
        }
      ]
    }),
    //
    retrieveThread: build.query<ThreadResponse, { threadId: string }>({
      query: ({ threadId }: {
        threadId: string
      }) => `v1/messaging/thread/${threadId}`,
      providesTags: (_result, _err, req) => [
        {
          type: `v1/messaging/thread/{threadId}`,
          id: `${req.threadId}`
        }
      ]
    })
    //
  })
});

export const {
  useInitializeThreadMutation,
  useLazyListThreadsQuery,
  useRetrieveThreadQuery
} = threadApi;

export const {
  endpoints: { initializeThread, listThreads, retrieveThread }
} = threadApi;
