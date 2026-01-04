import {
  BaseResponse, MaterialAccessType,
  MaterialInitializeFileUploadRequest,
  MaterialInitializeFileUploadResponse,
  MaterialInitializeFolderRequest,
  MaterialMoveRequest,
  MaterialRenameRequest
} from "@/model/be/jinear-core";
import { api } from "./api";

export const materialOperationApi = api.injectEndpoints({
  endpoints: (build) => ({
    //
    initializeFolder: build.mutation<BaseResponse, { workspaceId: string, body: MaterialInitializeFolderRequest }>({
      query: ({ workspaceId, body }) => ({
        url: `v1/material/operation/workspace/${workspaceId}/folder`,
        method: "POST",
        body: body
      }),
      invalidatesTags: ["v1/material/list/search"]
    }),
    //
    moveToFolder: build.mutation<BaseResponse, {
      body: MaterialMoveRequest
      onFulfilled?: () => void
    }>({
      query: ({ body }) => ({
        url: `v1/material/operation/move`,
        method: "PUT",
        body
      }),
      async onQueryStarted(props, { dispatch, queryFulfilled }) {
        try {
          await queryFulfilled;
        } catch (e) {
        }
        props?.onFulfilled?.();
      },
      invalidatesTags: ["v1/material/list/search"]
    }),
    //
    renameMaterial: build.mutation<BaseResponse, {
      body: MaterialRenameRequest
      onFulfilled?: () => void
    }>({
      query: ({ body }) => ({
        url: `v1/material/operation/rename`,
        method: "PUT",
        body
      }),
      async onQueryStarted(props, { dispatch, queryFulfilled }) {
        await queryFulfilled;
        props?.onFulfilled?.();
      },
      invalidatesTags: ["v1/material/list/search"]
    }),
    //
    initializeMaterialFileUpload: build.mutation<MaterialInitializeFileUploadResponse, {
      workspaceId: string,
      body: MaterialInitializeFileUploadRequest
    }>({
      query: ({ workspaceId, body }) => ({
        url: `v1/material/operation/workspace/${workspaceId}/file/upload-url`,
        method: "POST",
        body: body
      }),
      invalidatesTags: []
    }),
    //
    notifyMaterialUploaded: build.mutation<BaseResponse, { workspaceId: string, materialId: string }>({
      query: ({ workspaceId, materialId }) => ({
        url: `v1/material/operation/workspace/${workspaceId}/file/upload-url/notify/${materialId}`,
        method: "POST"
      }),
      invalidatesTags: ["v1/material/list/search"]
    }),
    //
    deleteMaterialPermanently: build.mutation<BaseResponse, { materialId: string }>({
      query: ({ materialId }) => ({
        url: `v1/material/operation/${materialId}/permanent`,
        method: "DELETE"
      }),
      invalidatesTags: ["v1/material/list/search", `v1/material/{materialId}`]
    }),
    //
    updateMaterialAccessType: build.mutation<BaseResponse, { materialId: string, accessType: MaterialAccessType }>({
      query: ({ materialId, accessType }) => ({
        url: `v1/material/operation/${materialId}/access-type/${accessType}`,
        method: "PUT"
      }),
      invalidatesTags: ["v1/material/list/search", `v1/material/{materialId}`]
    })
    //
  })
});

export const {
  useInitializeFolderMutation,
  useMoveToFolderMutation,
  useRenameMaterialMutation,
  useInitializeMaterialFileUploadMutation,
  useNotifyMaterialUploadedMutation,
  useDeleteMaterialPermanentlyMutation,
  useUpdateMaterialAccessTypeMutation
} = materialOperationApi;

export const {
  endpoints: {
    initializeFolder,
    moveToFolder,
    renameMaterial,
    initializeMaterialFileUpload,
    notifyMaterialUploaded,
    deleteMaterialPermanently,
    updateMaterialAccessType
  }
} = materialOperationApi;
