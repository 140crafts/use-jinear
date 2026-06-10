import { RichTextTempImageResponse } from "@/model/be/jinear-core";
import { api } from "./api";

export const richTextImageApi = api.injectEndpoints({
  endpoints: (build) => ({
    //
    uploadRichTextImage: build.mutation<RichTextTempImageResponse, {
      workspaceId: string,
      formData?: FormData
    }>({
      query: ({ workspaceId, formData }: { workspaceId: string, formData?: FormData }) => ({
        url: `/v1/rich-text/image/${workspaceId}/upload`,
        method: "POST",
        body: formData
      }),
      invalidatesTags: []
    })
    //
  })
});

export const {
  useUploadRichTextImageMutation
} = richTextImageApi;

export const {
  endpoints: {
    uploadRichTextImage
  }
} = richTextImageApi;
