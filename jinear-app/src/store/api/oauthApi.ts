import type {
    BaseResponse,
    OauthConnectionListingResponse,
    OauthConsentInfoResponse,
    OauthConsentResponse,
} from "@/model/be/jinear-core";
import {api} from "./api";

export const oauthApi = api.injectEndpoints({
    endpoints: (build) => ({
        retrieveOauthConnections: build.query<OauthConnectionListingResponse, void>({
            query: () => `v1/oauth/connection/list`,
            providesTags: () => [
                {
                    type: "v1/oauth/connection/list",
                },
            ],
        }),
        //
        revokeOauthConnection: build.mutation<BaseResponse, { oauthConnectionId: string }>({
            query: (req: { oauthConnectionId: string }) => ({
                url: `v1/oauth/connection/${req.oauthConnectionId}`,
                method: "DELETE",
            }),
            invalidatesTags: () => [
                {
                    type: "v1/oauth/connection/list",
                },
            ],
        }),
        //
        retrieveOauthConsentInfo: build.query<OauthConsentInfoResponse, { requestId: string }>({
            query: (req: { requestId: string }) => `v1/oauth/authorize/info/${req.requestId}`,
            providesTags: (_result, _err, req) => [
                {
                    type: "v1/oauth/authorize/info/{requestId}",
                    id: req.requestId,
                },
            ],
        }),
        //
        submitOauthConsent: build.mutation<OauthConsentResponse, { requestId: string; approved: boolean }>({
            query: (req: { requestId: string; approved: boolean }) => ({
                url: `v1/oauth/authorize/consent`,
                method: "POST",
                body: req,
            }),
            invalidatesTags: (_result, _err, req) => [
                {
                    type: "v1/oauth/authorize/info/{requestId}",
                    id: req.requestId,
                },
                {
                    type: "v1/oauth/connection/list",
                },
            ],
        }),
        //
    }),
});

export const {
    useRetrieveOauthConnectionsQuery,
    useRevokeOauthConnectionMutation,
    useRetrieveOauthConsentInfoQuery,
    useSubmitOauthConsentMutation,
} = oauthApi;
