import type {
    AuthCompleteRequest,
    AuthInitializeRequest,
    AuthInitializeResponse,
    AuthResponse, LoginWithAppleRequest,
    LoginWithPasswordRequest
} from "@/model/be/jinear-core";
import {api, tagTypes} from "./api";
import {LOGOUT_ENDPOINT_NAME, LOGOUT_PATH} from "./pendingLogout";

const invalidateAllOnSuccess = (_result: unknown, error: unknown) => error ? [] : tagTypes;

export const authApi = api.injectEndpoints({
    endpoints: (build) => ({
        [LOGOUT_ENDPOINT_NAME]: build.mutation<void, void>({
            query: () => ({
                url: LOGOUT_PATH,
                method: "POST"
            }),
            invalidatesTags: tagTypes
        }),
        emailLoginTokenRequest: build.mutation<AuthInitializeResponse, AuthInitializeRequest>({
            query: (body: AuthInitializeRequest) => ({
                url: "v1/auth/otp/email/initialize",
                method: "POST",
                body
            })
        }),
        emailOtpLoginComplete: build.mutation<AuthResponse, AuthCompleteRequest>({
            query: (body: AuthCompleteRequest) => ({
                url: "v1/auth/otp/email/complete",
                method: "POST",
                body
            }),
            invalidatesTags: invalidateAllOnSuccess
        }),
        loginWithPassword: build.mutation<AuthResponse, LoginWithPasswordRequest>({
            query: (body: LoginWithPasswordRequest) => ({
                url: "v1/auth/password/email",
                method: "POST",
                body
            }),
            invalidatesTags: invalidateAllOnSuccess
        }),
        //
        loginWithApple: build.mutation<AuthResponse, LoginWithAppleRequest>({
            query: (body: LoginWithAppleRequest) => ({
                url: "v1/auth/sign-in-with-apple",
                method: "POST",
                body
            }),
            invalidatesTags: invalidateAllOnSuccess
        })
        //
    })
});

export const {
    useLogoutMutation,
    useEmailLoginTokenRequestMutation,
    useEmailOtpLoginCompleteMutation,
    useLoginWithPasswordMutation,
    useLoginWithAppleMutation
} = authApi;

export const {
    endpoints: {logout, emailLoginTokenRequest, emailOtpLoginComplete, loginWithPassword}
} = authApi;
