import { AccountRetrieveResponse, BaseResponse, ConfirmEmailRequest, ResendConfirmEmailRequest } from "@/model/be/jinear-core";
import { api, tagTypes } from "./api";

export const accountApi = api.injectEndpoints({
  endpoints: (build) => ({
    me: build.query<AccountRetrieveResponse, void>({
      query: () => "v1/account",
      providesTags: ["Account-Current"],
    }),
    confirmEmail: build.mutation<BaseResponse, ConfirmEmailRequest>({
      query: (body: ConfirmEmailRequest) => ({
        url: "v1/account/confirm-email",
        method: "POST",
        body,
      }),
      invalidatesTags: tagTypes,
    }),
    //
    resendConfirmEmail: build.mutation<BaseResponse, ResendConfirmEmailRequest>({
      query: (body: ResendConfirmEmailRequest) => ({
        url: "v1/account/resend-confirm-email",
        method: "POST",
        body,
      }),
      invalidatesTags: tagTypes,
    }),
  }),
});

export const { useMeQuery, useConfirmEmailMutation, useResendConfirmEmailMutation } = accountApi;

export const {
  endpoints: { me, confirmEmail, resendConfirmEmail },
} = accountApi;
