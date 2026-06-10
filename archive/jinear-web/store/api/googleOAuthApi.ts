import { AuthRedirectInfoResponse } from "@/model/be/jinear-core";
import { api } from "./api";

export const googleOAuthApi = api.injectEndpoints({
  endpoints: (build) => ({
    retrieveLoginRedirectInfo: build.query<AuthRedirectInfoResponse, void>({
      query: (req) => `v1/oauth/google/redirect-info/login`,
      providesTags: (_result, _err, req) => [
        {
          type: "v1/oauth/google/redirect-info/login",
        },
      ],
    }),
    //
    retrieveAttachMailRedirectInfo: build.query<AuthRedirectInfoResponse, { workspaceId: string }>({
      query: (req: { workspaceId: string }) => `v1/oauth/google/redirect-info/attach-mail?workspaceId=${req.workspaceId}`,
      providesTags: (_result, _err, req) => [
        {
          type: "v1/oauth/google/redirect-info/attach-mail",
          id: req.workspaceId,
        },
      ],
    }),
    //
    retrieveAttachCalendarRedirectInfo: build.query<AuthRedirectInfoResponse, { workspaceId: string }>({
      query: (req: { workspaceId: string }) => `v1/oauth/google/redirect-info/attach-calendar?workspaceId=${req.workspaceId}`,
      providesTags: (_result, _err, req) => [
        {
          type: "v1/oauth/google/redirect-info/attach-calendar",
          id: req.workspaceId,
        },
      ],
    }),
    //
  }),
});

export const {
  useRetrieveLoginRedirectInfoQuery,
  useRetrieveAttachMailRedirectInfoQuery,
  useRetrieveAttachCalendarRedirectInfoQuery,
} = googleOAuthApi;

export const {
  endpoints: { retrieveLoginRedirectInfo, retrieveAttachMailRedirectInfo },
} = googleOAuthApi;
