import { api } from "./api";
import { MessagingTokenResponse } from "@/be/jinear-core";

export const messageTokenApi = api.injectEndpoints({
  endpoints: (build) => ({
    //
    retrieveMessagingToken: build.query<MessagingTokenResponse, {}>({
      query: () => `v1/messaging/token`,
      providesTags: (_result, _err, req) => [
        { type: `v1/messaging/token` }
      ]
    })
    //
  })
});

export const { useRetrieveMessagingTokenQuery } = messageTokenApi;

export const {
  endpoints: { retrieveMessagingToken }
} = messageTokenApi;
