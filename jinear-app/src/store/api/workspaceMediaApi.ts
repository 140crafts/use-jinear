import type { BaseResponse, LocaleType, WorkspaceMediaLimitResponse } from "@/model/be/jinear-core";
import { api } from "./api";

export interface IWorkspaceInitializeRequest {
  workspaceId: string;
  formData?: FormData;
  locale?: LocaleType;
}

export const workspaceMediaApi = api.injectEndpoints({
  endpoints: (build) => ({
    //
    updateWorkspaceProfilePicture: build.mutation<BaseResponse, IWorkspaceInitializeRequest>({
      query: (request: IWorkspaceInitializeRequest) => ({
        url: `v1/workspace/media/${request.workspaceId}/profile-picture`,
        method: "POST",
        body: request.formData
      }),
      invalidatesTags: (result) => (result == null ? [] : ["v1/account"])
    }),
    //
    retrieveWorkspaceMediaLimits: build.query<WorkspaceMediaLimitResponse, { workspaceId: string }>({
      query: ({ workspaceId }: { workspaceId: string }) => `v1/workspace/media/${workspaceId}/limits`,
      providesTags: (_result, _err, { workspaceId }) => [
        {
          type: `v1/workspace/media/{workspaceId}/limits`,
          id: `${workspaceId}`
        }
      ]
    })
    //
  })
});

export const { useUpdateWorkspaceProfilePictureMutation, useRetrieveWorkspaceMediaLimitsQuery } = workspaceMediaApi;

export const {
  endpoints: { updateWorkspaceProfilePicture, retrieveWorkspaceMediaLimits }
} = workspaceMediaApi;
