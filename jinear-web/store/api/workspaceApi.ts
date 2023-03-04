import { BaseResponse, WorkspaceInitializeRequest } from "@/model/be/jinear-core";
import { api } from "./api";

export const workspaceApi = api.injectEndpoints({
  endpoints: (build) => ({
    initializeWorkspace: build.mutation<BaseResponse, WorkspaceInitializeRequest>({
      query: (body: WorkspaceInitializeRequest) => ({
        url: `v1/workspace`,
        method: "POST",
        body,
      }),
      invalidatesTags: ["Account-Current"],
    }),
  }),
});

export const { useInitializeWorkspaceMutation } = workspaceApi;

export const {
  endpoints: { initializeWorkspace },
} = workspaceApi;
