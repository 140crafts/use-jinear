import { BaseResponse, MaterialAccessPaginatedResponse, MaterialSearchRequest } from "@/model/be/jinear-core";
import { api } from "./api";

export const materialAccessApi = api.injectEndpoints({
  endpoints: (build) => ({
    //
    listMaterialAccess: build.query<MaterialAccessPaginatedResponse, { materialId: string }>({
      query: ({ materialId }: { materialId: string }) => {
        return {
          url: `v1/material/access/${materialId}`,
          method: "GET"
        };
      },
      providesTags: [`v1/material/access/{materialId}`]
    }),
    //
    giveAccess: build.mutation<BaseResponse, { materialId: string, accountId: string }>({
      query: ({ materialId, accountId }) => ({
        url: `v1/material/access/${materialId}/account/${accountId}`,
        method: "POST"
      }),
      invalidatesTags: [
        "v1/material/list/search",
        "v1/material/access/{materialId}"
      ]
    }),
    //
    revokeAccess: build.mutation<BaseResponse, { materialId: string, accountId: string }>({
      query: ({ materialId, accountId }) => ({
        url: `v1/material/access/${materialId}/account/${accountId}`,
        method: "DELETE"
      }),
      invalidatesTags: [
        "v1/material/list/search",
        "v1/material/access/{materialId}"
      ]
    })
    //
  })
});

export const {
  useListMaterialAccessQuery,
  useGiveAccessMutation,
  useRevokeAccessMutation
} = materialAccessApi;

export const {
  endpoints: {
    listMaterialAccess,
    giveAccess,
    revokeAccess
  }
} = materialAccessApi;
