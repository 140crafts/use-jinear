import type {WorkspacePrefetchRequest, WorkspacePrefetchResponse} from "@/model/be/jinear-core";
import {api} from "./api";

export const workspacePrefetchApi = api.injectEndpoints({
    endpoints: (build) => ({
        prefetchWorkspace: build.mutation<WorkspacePrefetchResponse, WorkspacePrefetchRequest>({
            query: (req) => ({url: `v1/workspace/prefetch`, method: "POST", body: req})
        })
        //
    })
});
