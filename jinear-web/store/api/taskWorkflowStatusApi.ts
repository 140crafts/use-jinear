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
        "v1/task/from-workspace/{workspaceName}/{taskTag}",
        "v1/task/list/filter",
        "v1/task-board/entry/from-task-board/{taskBoardId}",
        "v1/workspace/activity/filter",
        "v1/task-analytics/{workspaceId}/team/{teamId}",
      ],
    }),
    //
  }),
});

export const { useUpdateTaskWorkflowStatusMutation } = taskWorkflowStatusApi;

export const {
  endpoints: { updateTaskWorkflowStatus },
} = taskWorkflowStatusApi;
