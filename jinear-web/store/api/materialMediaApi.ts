import { BaseResponse, MediaVisibilityType } from "@/model/be/jinear-core";
import { api } from "./api";

export const materialMediaApi = api.injectEndpoints({
  endpoints: (build) => ({
    //
    changeMaterialRelatedMediaAccess: build.mutation<BaseResponse, {
      materialId: string,
      mediaVisibilityType: MediaVisibilityType
    }>({
      query: ({ materialId, mediaVisibilityType }) => ({
        url: `v1/material/media/${materialId}/update-visibility/${mediaVisibilityType}`,
        method: "POST"
      }),
      invalidatesTags: ["v1/material/list/search"]
    })
    //
  })
});

export const {
  useChangeMaterialRelatedMediaAccessMutation
} = materialMediaApi;

export const {
  endpoints: {
    changeMaterialRelatedMediaAccess
  }
} = materialMediaApi;
