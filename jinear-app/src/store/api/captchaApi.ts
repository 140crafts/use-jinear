import type {CaptchaChallengeResponse} from "@/model/be/jinear-core";
import {api} from "./api";

export const captchaApi = api.injectEndpoints({
    endpoints: (build) => ({
        //
        generateCaptcha: build.query<CaptchaChallengeResponse, {}>({
            query: () => `v1/captcha/generate`,
            providesTags: (_result, _err, req) => ["v1/captcha/generate"]
        })
        //
    })
});

export const {useLazyGenerateCaptchaQuery} = captchaApi;

export const {
    endpoints: {generateCaptcha}
} = captchaApi;
