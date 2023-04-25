import { BaseResponse, InitializeChecklistRequest, RetrieveChecklistResponse } from "@/model/be/jinear-core";
import { api } from "./api";

export const taskChecklistApi = api.injectEndpoints({
  endpoints: (build) => ({
    initializeChecklist: build.mutation<BaseResponse, InitializeChecklistRequest>({
      query: (req) => ({
        url: `v1/task/checklist`,
        method: "POST",
        body: req,
      }),
      invalidatesTags: ["workplace-task-with-name-and-tag", "task-checklist"],
    }),
    //
    retrieveChecklist: build.query<
      RetrieveChecklistResponse,
      {
        checklistId: string;
      }
    >({
      query: (req: { checklistId: string }) => `v1/task/checklist/${req.checklistId}`,
      providesTags: (_result, _err, req) => [
        {
          type: "task-checklist",
          id: `${req.checklistId}`,
        },
      ],
    }),
    //
    passivizeChecklist: build.mutation<
      BaseResponse,
      {
        checklistId: string;
      }
    >({
      query: (req) => ({
        url: `v1/task/checklist/${req.checklistId}`,
        method: "DELETE",
      }),
      invalidatesTags: (_result, _err, req) => ["workplace-task-with-name-and-tag"],
    }),
    //
    updateChecklistLabel: build.mutation<
      BaseResponse,
      {
        checklistId: string;
        title: string;
      }
    >({
      query: (req) => ({
        url: `v1/task/checklist/${req.checklistId}/label`,
        method: "PUT",
        body: req,
      }),
      invalidatesTags: (_result, _err, req) => [
        "workplace-task-with-name-and-tag",
        { type: "task-checklist", id: req.checklistId },
      ],
    }),
    //
  }),
});

export const {
  useInitializeChecklistMutation,
  useRetrieveChecklistQuery,
  useUpdateChecklistLabelMutation,
  usePassivizeChecklistMutation,
} = taskChecklistApi;

export const {
  endpoints: { initializeChecklist, retrieveChecklist, updateChecklistLabel, passivizeChecklist },
} = taskChecklistApi;
