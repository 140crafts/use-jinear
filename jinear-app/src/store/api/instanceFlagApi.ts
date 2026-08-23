import type {InstanceFlagListingResponse} from "@/model/be/jinear-core";
import {api} from "./api";

export const instanceFlagApi = api.injectEndpoints({
    endpoints: (build) => ({
        retrieveInstanceFlags: build.query<InstanceFlagListingResponse, void>({
            query: () => `v1/instance-flag/list`,
            providesTags: () => ["v1/instance-flag/list"]
        })
    })
});

export const {useRetrieveInstanceFlagsQuery} = instanceFlagApi;

export const {endpoints: {retrieveInstanceFlags}} = instanceFlagApi;
