import {
  AccountRetrieveResponse,
  BaseResponse,
  ConfirmEmailRequest,
} from "@/model/be/jinear-core";
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
  }),
});

export const { useMeQuery, useConfirmEmailMutation } = accountApi;

export const {
  endpoints: { me, confirmEmail },
} = accountApi;
