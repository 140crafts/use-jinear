import { TaskResponse } from "@/model/be/jinear-core";
import { api } from "./api";

export const taskWorkflowStatusApi = api.injectEndpoints({
  endpoints: (build) => ({
    updateTaskWorkflowStatus: build.mutation<
      TaskResponse,
      { taskId: string; workflowStatusId: string }
    >({
      query: (body: { taskId: string; workflowStatusId: string }) => ({
        url: `v1/task/workflow-status/${body.taskId}/${body.workflowStatusId}`,
        method: "PUT",
      }),
      invalidatesTags: ["team-task-list"],
    }),
    //
  }),
});

export const { useUpdateTaskWorkflowStatusMutation } = taskWorkflowStatusApi;

export const {
  endpoints: { updateTaskWorkflowStatus },
} = taskWorkflowStatusApi;
