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
        "retrieve-task-activity",
        "team-task-list",
        "workspace-task-list",
        "workplace-task-with-name-and-tag",
        "team-workflow-task-list",
      ],
    }),
    //
    deleteTaskRelation: build.mutation<BaseResponse, { relationId: string }>({
      query: (body: { relationId: string }) => ({
        url: `v1/task/relation/${body.relationId}`,
        method: "DELETE",
      }),
      invalidatesTags: (_result, _err, req) => [
        "retrieve-task-activity",
        "team-task-list",
        "workspace-task-list",
        "workplace-task-with-name-and-tag",
        "team-workflow-task-list",
      ],
    }),
    //
  }),
});

export const { useInitializeTaskRelationMutation, useDeleteTaskRelationMutation } = taskRelationApi;

export const {
  endpoints: { initializeTaskRelation, deleteTaskRelation },
} = taskRelationApi;
