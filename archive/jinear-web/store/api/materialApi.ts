import { MaterialRetrieveResponse } from "@/model/be/jinear-core";
import { api } from "./api";

export const materialApi = api.injectEndpoints({
  endpoints: (build) => ({
    //
    retrieveMaterial: build.query<MaterialRetrieveResponse, { materialId: string }>({
      query: ({ materialId }: { materialId: string }) => {
        return {
          url: `v1/material/${materialId}`,
          method: "GET"
        };
      }
      ,
      providesTags: [`v1/material/{materialId}`]
    })
//
  })
});

export const {
  useRetrieveMaterialQuery
} = materialApi;

export const {
  endpoints: {
    retrieveMaterial
  }
} = materialApi;
