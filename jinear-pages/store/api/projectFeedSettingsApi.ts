import { BaseResponse, ProjectFeedSettingsOperationRequest } from "@/model/be/jinear-core";
import { api } from "./api";

export const projectFeedSettingsApi = api.injectEndpoints({
  endpoints: (build) => ({
    //
    updateProjectFeedSettings: build.mutation<BaseResponse, ProjectFeedSettingsOperationRequest>({
      query: (req) => ({
        url: "v1/project/feed/settings",
        method: "PUT",
        body: req
      }),
      invalidatesTags: ["v1/project/query/assigned/{workspaceId}", "v1/project/query/all/{workspaceId}", "v1/project/query/archived/{workspaceId}", `v1/project/query/{projectId}`]
    })
    //
  })
});

export const { useUpdateProjectFeedSettingsMutation } = projectFeedSettingsApi;

export const {
  endpoints: { updateProjectFeedSettings }
} = projectFeedSettingsApi;
