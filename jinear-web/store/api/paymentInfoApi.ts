import { RetrieveSubscriptionInfoResponse } from "@/model/be/jinear-core";
import { api } from "./api";

export const paymentInfoApi = api.injectEndpoints({
  endpoints: (build) => ({
    //
    retrieveSubscriptionInfo: build.query<RetrieveSubscriptionInfoResponse, { workspaceId: string }>({
      query: (req: { workspaceId: string }) => ({
        url: `v1/payments/info/workspace/${req.workspaceId}/subscription`,
      }),
      providesTags: (_result, _err, req) => [
        {
          type: "v1/payments/info/workspace/{workspaceId}/subscription",
          id: `${req.workspaceId}`,
        },
      ],
    }),
    //
  }),
});

export const { useRetrieveSubscriptionInfoQuery } = paymentInfoApi;

export const {
  endpoints: { retrieveSubscriptionInfo },
} = paymentInfoApi;
