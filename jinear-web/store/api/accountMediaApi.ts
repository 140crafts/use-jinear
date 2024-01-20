import { BaseResponse } from "@/model/be/jinear-core";
import { api } from "./api";

export interface IUpdateProfilePictureRequest {
  formData?: FormData;
}

export const accountMediaApi = api.injectEndpoints({
  endpoints: (build) => ({
    updateProfilePicture: build.mutation<BaseResponse, IUpdateProfilePictureRequest>({
      query: (request: IUpdateProfilePictureRequest) => ({
        url: `v1/account/media/profile-picture`,
        method: "POST",
        body: request.formData,
      }),
      invalidatesTags: (result) => (result == null ? [] : ["v1/account"]),
    }),
  }),
});

export const { useUpdateProfilePictureMutation } = accountMediaApi;

export const {
  endpoints: { updateProfilePicture },
} = accountMediaApi;
