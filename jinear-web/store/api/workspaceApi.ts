import { BaseResponse, WorkspaceInitializeRequest } from "@/model/be/jinear-core";
import { api } from "./api";

export interface IWorkspaceInitializeRequest extends WorkspaceInitializeRequest {
  formData?: FormData;
}

const generateQueryParams = (request: IWorkspaceInitializeRequest) => {
  return (
    "?" +
    Object.keys(request)
      //@ts-ignore
      .filter((key) => request[key] != null)
      //@ts-ignore
      .map((key) => `${key}=${encodeURIComponent(request[key])}`)
      .join("&")
  );
};

export const workspaceApi = api.injectEndpoints({
  endpoints: (build) => ({
    initializeWorkspace: build.mutation<BaseResponse, IWorkspaceInitializeRequest>({
      query: (request: IWorkspaceInitializeRequest) => ({
        url: `v1/workspace` + generateQueryParams({ ...request, formData: undefined }),
        method: "POST",
        body: request.formData,
      }),
      invalidatesTags: (result) => (result == null ? [] : ["Account-Current"]),
    }),
  }),
});

export const { useInitializeWorkspaceMutation } = workspaceApi;

export const {
  endpoints: { initializeWorkspace },
} = workspaceApi;
