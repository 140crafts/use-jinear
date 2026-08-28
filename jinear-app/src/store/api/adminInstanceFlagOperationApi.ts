import type {BaseResponse, InstanceFlagOperationRequest} from "@/model/be/jinear-core";
import {api} from "./api";

export const adminInstanceFlagOperationApi = api.injectEndpoints({
    endpoints: (build) => ({
        //
        setInstanceFlag: build.mutation<BaseResponse, InstanceFlagOperationRequest>({
            query: (body: InstanceFlagOperationRequest) => ({
                url: `v1/admin/instance-flag/operation`,
                method: "POST",
                body
            }),
            invalidatesTags: ["v1/instance-flag/list"]
        })
        //
    })
});

export const {useSetInstanceFlagMutation} = adminInstanceFlagOperationApi;

export const {endpoints: {setInstanceFlag}} = adminInstanceFlagOperationApi;
