import { BaseResponse } from "@/model/be/jinear-core";
import { api, tagTypes } from "./api";

export const paymentProcessApi = api.injectEndpoints({
  endpoints: (build) => ({
    //
    refreshPayments: build.mutation<BaseResponse, void>({
      query: () => ({
        url: "v1/payments/process/refresh",
        method: "POST",
      }),
      invalidatesTags: tagTypes,
    }),
    //
  }),
});

export const { useRefreshPaymentsMutation } = paymentProcessApi;

export const {
  endpoints: { refreshPayments },
} = paymentProcessApi;
