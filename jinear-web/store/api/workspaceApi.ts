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
      invalidatesTags: (result) =>
        result == null
          ? []
          : [
              "v1/account",
              "v1/workspace/member/{workspaceId}/list",
              "v1/team/from-workspace/{workspaceId}",
              "v1/team/member/list/{teamId}",
              "v1/topic/list/{teamId}",
              "v1/workspace/activity/filter",
            ],
    }),
  }),
});

export const { useInitializeWorkspaceMutation } = workspaceApi;

export const {
  endpoints: { initializeWorkspace },
} = workspaceApi;
