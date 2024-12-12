import { AccountDeletionEligibilityResponse, BaseResponse } from "@/model/be/jinear-core";
import { api, tagTypes } from "./api";

export const accountDeleteApi = api.injectEndpoints({
  endpoints: (build) => ({
    sendAccountDeleteEmail: build.mutation<BaseResponse, void>({
      query: () => ({
        url: "v1/account/delete",
        method: "POST",
      }),
      invalidatesTags: [],
    }),
    //
    checkEligibility: build.query<AccountDeletionEligibilityResponse, void>({
      query: () => "v1/account/delete/eligibility",
      providesTags: ["v1/account/delete/eligibility"],
    }),
    //
    confirmAccountDelete: build.mutation<BaseResponse, string>({
      query: (token: string) => ({
        url: `v1/account/delete/confirm/${token}`,
        method: "DELETE",
      }),
      invalidatesTags: tagTypes,
    }),
    //
    deleteWithoutConfirmation: build.mutation<BaseResponse, void>({
      query: () => ({
        url: `v1/account/delete`,
        method: "DELETE",
      }),
      invalidatesTags: tagTypes,
    }),
    //
  }),
});

export const {
  useSendAccountDeleteEmailMutation,
  useCheckEligibilityQuery,
  useLazyCheckEligibilityQuery,
  useConfirmAccountDeleteMutation,
  useDeleteWithoutConfirmationMutation,
} = accountDeleteApi;

export const {
  endpoints: { sendAccountDeleteEmail, checkEligibility, confirmAccountDelete, deleteWithoutConfirmation },
} = accountDeleteApi;
