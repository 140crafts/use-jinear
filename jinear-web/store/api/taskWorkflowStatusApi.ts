import { TaskResponse } from "@/model/be/jinear-core";
import { api } from "./api";

export const taskWorkflowStatusApi = api.injectEndpoints({
  endpoints: (build) => ({
    updateTaskWorkflowStatus: build.mutation<TaskResponse, { taskId: string; workflowStatusId: string }>({
      query: (body: { taskId: string; workflowStatusId: string }) => ({
        url: `v1/task/workflow-status/${body.taskId}/${body.workflowStatusId}`,
        method: "PUT",
      }),
      invalidatesTags: (_result, _err, req) => [
        { type: "team-task-list" },
        { type: "workspace-task-list" },
        { type: "workplace-task-with-name-and-tag" },
        { type: "team-workflow-task-list" },
        { type: "team-topic-task-list" },
        { type: "team-all-task-list" },
        { type: "retrieve-task-activity", id: req.taskId },
      ],
    }),
    //
  }),
});

export const { useUpdateTaskWorkflowStatusMutation } = taskWorkflowStatusApi;

export const {
  endpoints: { updateTaskWorkflowStatus },
} = taskWorkflowStatusApi;
