import { BaseResponse, LocaleType } from "@/model/be/jinear-core";
import { api } from "./api";

export interface IWorkspaceInitializeRequest {
  workspaceId: string;
  formData?: FormData;
  locale?: LocaleType;
}

export const workspaceMediaApi = api.injectEndpoints({
  endpoints: (build) => ({
    updateWorkspaceProfilePicture: build.mutation<BaseResponse, IWorkspaceInitializeRequest>({
      query: (request: IWorkspaceInitializeRequest) => ({
        url: `v1/workspace/media/${request.workspaceId}/profile-picture`,
        method: "POST",
        body: request.formData,
      }),
      invalidatesTags: (result) => (result == null ? [] : ["v1/account"]),
    }),
  }),
});

export const { useUpdateWorkspaceProfilePictureMutation } = workspaceMediaApi;

export const {
  endpoints: { updateWorkspaceProfilePicture },
} = workspaceMediaApi;
