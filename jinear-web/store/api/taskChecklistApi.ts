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
      invalidatesTags: (_result, _err, req) => [
        "v1/task/from-workspace/{workspaceName}/{taskTag}",
        "v1/task/checklist",
        "v1/workspace/activity/filter",
      ],
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
          type: "v1/task/checklist",
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
      invalidatesTags: (_result, _err, req) => [
        "v1/task/from-workspace/{workspaceName}/{taskTag}",
        "v1/workspace/activity/filter",
      ],
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
        "v1/task/from-workspace/{workspaceName}/{taskTag}",
        { type: "v1/task/checklist", id: req.checklistId },
        "v1/workspace/activity/filter",
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
