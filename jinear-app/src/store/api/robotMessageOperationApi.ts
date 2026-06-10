import type {BaseResponse, SendMessageRequest} from "@/model/be/jinear-core";
import {api} from "./api";

export const robotMessageOperationApi = api.injectEndpoints({
    endpoints: (build) => ({
        //
        sendToThreadUsingRobots: build.mutation<BaseResponse, {
            body: SendMessageRequest,
            threadId: string,
            robotToken: string
        }>({
            query: ({body, threadId, robotToken}: {
                body: SendMessageRequest,
                threadId: string,
                robotToken: string
            }) => ({
                url: `v1/robots/messaging/message/operation/thread`,
                method: "POST",
                body,
                headers: {
                    "X-THREAD-ID": threadId,
                    "Authorization": `Bearer ${robotToken}`
                }
            }),
            invalidatesTags: (_result, _err, req) => []
        })
        //
    })
});

export const {useSendToThreadUsingRobotsMutation} = robotMessageOperationApi;

export const {
    endpoints: {sendToThreadUsingRobots}
} = robotMessageOperationApi;
