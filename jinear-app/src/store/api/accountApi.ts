import type {
    AccountRetrieveResponse,
    BaseResponse,
    ConfirmEmailRequest,
    ResendConfirmEmailRequest
} from "@/model/be/jinear-core";
import {api} from "./api";

export const accountApi = api.injectEndpoints({
    endpoints: (build) => ({
        me: build.query<AccountRetrieveResponse, void>({
            query: () => ({url: "v1/account", timeout: 8000}),
            providesTags: ["v1/account"],
        }),
        confirmEmail: build.mutation<BaseResponse, ConfirmEmailRequest>({
            query: (body: ConfirmEmailRequest) => ({
                url: "v1/account/confirm-email",
                method: "POST",
                body,
            }),
        }),
        //
        resendConfirmEmail: build.mutation<BaseResponse, ResendConfirmEmailRequest>({
            query: (body: ResendConfirmEmailRequest) => ({
                url: "v1/account/resend-confirm-email",
                method: "POST",
                body,
            }),
        }),
        //
    }),
});

export const {useMeQuery, useConfirmEmailMutation, useResendConfirmEmailMutation} = accountApi;

export const {
    endpoints: {me, confirmEmail, resendConfirmEmail},
} = accountApi;
