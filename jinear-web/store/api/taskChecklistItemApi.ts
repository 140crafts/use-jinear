import { BaseResponse, ChecklistItemLabelRequest } from "@/model/be/jinear-core";
import { api } from "./api";

export const taskChecklistItemApi = api.injectEndpoints({
  endpoints: (build) => ({
    initializeChecklistItem: build.mutation<BaseResponse, ChecklistItemLabelRequest>({
      query: (req) => ({
        url: `v1/task/checklist/item`,
        method: "POST",
        body: req,
      }),
      invalidatesTags: ["task-checklist"],
    }),
    //
    updateCheckedStatus: build.mutation<
      BaseResponse,
      {
        checklistItemId: string;
        checked: boolean;
      }
    >({
      query: (req) => ({
        url: `v1/task/checklist/item/${req.checklistItemId}/${req.checked}`,
        method: "PUT",
      }),
      invalidatesTags: (_result, _err, req) => [],
    }),
    //
    updateLabel: build.mutation<
      BaseResponse,
      {
        checklistId: string;
        checklistItemId: string;
        label: string;
      }
    >({
      query: (req) => ({
        url: `v1/task/checklist/item/${req.checklistItemId}/label`,
        method: "PUT",
        body: req,
      }),
      invalidatesTags: (_result, _err, req) => [],
    }),
    //
    deleteChecklistItem: build.mutation<
      BaseResponse,
      {
        checklistItemId: string;
      }
    >({
      query: (req) => ({
        url: `v1/task/checklist/item/${req.checklistItemId}`,
        method: "DELETE",
      }),
      invalidatesTags: ["task-checklist"],
    }),
    //
  }),
});

export const {
  useInitializeChecklistItemMutation,
  useUpdateCheckedStatusMutation,
  useUpdateLabelMutation,
  useDeleteChecklistItemMutation,
} = taskChecklistItemApi;

export const {
  endpoints: { initializeChecklistItem, updateCheckedStatus, updateLabel, deleteChecklistItem },
} = taskChecklistItemApi;
