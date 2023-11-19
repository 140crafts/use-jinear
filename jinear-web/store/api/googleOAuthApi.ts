import { AuthRedirectInfoResponse } from "@/model/be/jinear-core";
import { api } from "./api";

export const googleOAuthApi = api.injectEndpoints({
  endpoints: (build) => ({
    retrieveLoginRedirectInfo: build.query<AuthRedirectInfoResponse, void>({
      query: (req) => `v1/oauth/google/login-redirect-info`,
      providesTags: (_result, _err, req) => [
        {
          type: "oauth/google/login-redirect-info",
        },
      ],
    }),
    //
  }),
});

export const { useRetrieveLoginRedirectInfoQuery } = googleOAuthApi;

export const {
  endpoints: { retrieveLoginRedirectInfo },
} = googleOAuthApi;
