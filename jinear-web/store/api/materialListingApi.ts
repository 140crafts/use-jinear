import { MaterialSearchRequest, ParentMaterialDtoResponse } from "@/model/be/jinear-core";
import { api } from "./api";

export const materialListingApi = api.injectEndpoints({
  endpoints: (build) => ({
    searchMaterial: build.query<ParentMaterialDtoResponse, MaterialSearchRequest>({
      query: (body) => {
        return {
          url: `v1/material/list/search`,
          method: "POST",
          body
        };
      },
      providesTags: (_result, _err, materialSearchRequest) => [
        {
          type: `v1/material/list/search`,
          id: `${JSON.stringify(materialSearchRequest)}`
        }
      ]
    })
    //

  })
});

export const { useSearchMaterialQuery } = materialListingApi;

export const {
  endpoints: { searchMaterial }
} = materialListingApi;
