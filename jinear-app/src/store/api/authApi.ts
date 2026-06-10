import type {
    AuthCompleteRequest,
    AuthInitializeRequest,
    AuthInitializeResponse,
    AuthResponse, LoginWithAppleRequest,
    LoginWithPasswordRequest
} from "@/model/be/jinear-core";
import {api, tagTypes} from "./api";

export const authApi = api.injectEndpoints({
    endpoints: (build) => ({
        logout: build.mutation<void, void>({
            query: () => ({
                url: "v1/auth/logout",
                method: "POST"
            }),
            invalidatesTags: tagTypes
        }),
        emailLoginTokenRequest: build.mutation<AuthInitializeResponse, AuthInitializeRequest>({
            query: (body: AuthInitializeRequest) => ({
                url: "v1/auth/otp/email/initialize",
                method: "POST",
                body
            }),
            invalidatesTags: tagTypes
        }),
        emailOtpLoginComplete: build.mutation<AuthResponse, AuthCompleteRequest>({
            query: (body: AuthCompleteRequest) => ({
                url: "v1/auth/otp/email/complete",
                method: "POST",
                body
            }),
            invalidatesTags: tagTypes
        }),
        loginWithPassword: build.mutation<AuthResponse, LoginWithPasswordRequest>({
            query: (body: LoginWithPasswordRequest) => ({
                url: "v1/auth/password/email",
                method: "POST",
                body
            }),
            invalidatesTags: tagTypes
        }),
        //
        loginWithApple: build.mutation<AuthResponse, LoginWithAppleRequest>({
            query: (body: LoginWithAppleRequest) => ({
                url: "v1/auth/sign-in-with-apple",
                method: "POST",
                body
            }),
            invalidatesTags: tagTypes
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
