import { BaseResponse, TaskRelationInitializeRequest } from "@/model/be/jinear-core";
import { api } from "./api";

export const taskRelationApi = api.injectEndpoints({
  endpoints: (build) => ({
    initializeTaskRelation: build.mutation<BaseResponse, TaskRelationInitializeRequest>({
      query: (req: TaskRelationInitializeRequest) => ({
        url: `v1/task/relation`,
        method: "POST",
        body: req,
      }),
      invalidatesTags: (_result, _err, req) => [
        "v1/workspace/activity/filter",
        "v1/task/from-workspace/{workspaceName}/{taskTag}",
      ],
    }),
    //
    deleteTaskRelation: build.mutation<BaseResponse, { relationId: string }>({
      query: (body: { relationId: string }) => ({
        url: `v1/task/relation/${body.relationId}`,
        method: "DELETE",
      }),
      invalidatesTags: (_result, _err, req) => [
        "v1/workspace/activity/filter",
        "v1/task/from-workspace/{workspaceName}/{taskTag}",
      ],
    }),
    //
  }),
});

export const { useInitializeTaskRelationMutation, useDeleteTaskRelationMutation } = taskRelationApi;

export const {
  endpoints: { initializeTaskRelation, deleteTaskRelation },
} = taskRelationApi;
