import { BaseResponse, RegisterViaMailRequest } from "@/model/be/jinear-core";
import { api, tagTypes } from "./api";

export const accountRegisterApi = api.injectEndpoints({
  endpoints: (build) => ({
    registerViaMail: build.mutation<BaseResponse, RegisterViaMailRequest>({
      query: (body: RegisterViaMailRequest) => ({
        url: "v1/account/register/email",
        method: "POST",
        body,
      }),
      invalidatesTags: tagTypes,
    }),
  }),
});

export const { useRegisterViaMailMutation } = accountRegisterApi;

export const {
  endpoints: { registerViaMail },
} = accountRegisterApi;
