import {
  BaseResponse,
  CompleteResetPasswordRequest,
  InitializeResetPasswordRequest,
  UpdatePasswordRequest,
} from "@/model/be/jinear-core";
import { api, tagTypes } from "./api";

export const accountPasswordApi = api.injectEndpoints({
  endpoints: (build) => ({
    initializeResetPassword: build.mutation<
      BaseResponse,
      InitializeResetPasswordRequest
    >({
      query: (body: InitializeResetPasswordRequest) => ({
        url: "v1/account/password/reset/initialize",
        method: "POST",
        body,
      }),
      invalidatesTags: tagTypes,
    }),
    completeResetPassword: build.mutation<
      BaseResponse,
      CompleteResetPasswordRequest
    >({
      query: (body: CompleteResetPasswordRequest) => ({
        url: "v1/account/password/reset/complete",
        method: "POST",
        body,
      }),
      invalidatesTags: tagTypes,
    }),
    updatePassword: build.mutation<BaseResponse, UpdatePasswordRequest>({
      query: (body: UpdatePasswordRequest) => ({
        url: "v1/account/password/update",
        method: "POST",
        body,
      }),
      invalidatesTags: tagTypes,
    }),
  }),
});

export const {
  useInitializeResetPasswordMutation,
  useCompleteResetPasswordMutation,
  useUpdatePasswordMutation,
} = accountPasswordApi;

export const {
  endpoints: { initializeResetPassword, completeResetPassword, updatePassword },
} = accountPasswordApi;
