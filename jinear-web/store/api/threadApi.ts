import { BaseResponse, InitializeThreadRequest, ThreadListingResponse } from "@/model/be/jinear-core";
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
        await queryFulfilled
        setTimeout(() => {
          dispatch(api.util.invalidateTags([`v1/messaging/thread/channel/{channelId}`]))
        }, 1500)
      },
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
    })
    //
  })
});

export const {
  useInitializeThreadMutation,
  useLazyListThreadsQuery
} = threadApi;

export const {
  endpoints: { initializeThread, listThreads }
} = threadApi;
