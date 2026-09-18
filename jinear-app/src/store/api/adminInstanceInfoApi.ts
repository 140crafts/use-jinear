import type {InstanceInfoResponse} from "@/model/be/jinear-core";
import {api} from "./api";

export const adminInstanceInfoApi = api.injectEndpoints({
    endpoints: (build) => ({
        retrieveInstanceInfo: build.query<InstanceInfoResponse, void>({
            query: () => `v1/admin/instance-info`,
            providesTags: () => ["v1/admin/instance-info"]
        })
    })
});

export const {useRetrieveInstanceInfoQuery} = adminInstanceInfoApi;

export const {endpoints: {retrieveInstanceInfo}} = adminInstanceInfoApi;
