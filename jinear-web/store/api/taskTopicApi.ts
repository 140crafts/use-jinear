import { TaskResponse } from "@/model/be/jinear-core";
import { api } from "./api";

export const taskTopicApi = api.injectEndpoints({
  endpoints: (build) => ({
    updateTaskTopic: build.mutation<TaskResponse, { taskId: string; topicId: string }>({
      query: (body: { taskId: string; topicId: string }) => ({
        url: `v1/task/topic/${body.taskId}/${body.topicId}`,
        method: "PUT",
      }),
      invalidatesTags: (_result, _err, req) => [
        { type: "v1/task/from-workspace/{workspaceName}/{taskTag}" },
        "v1/task/list/filter",
        "v1/workspace/activity/filter",
        "v1/task-analytics/{workspaceId}/team/{teamId}",
      ],
    }),
    //
    removeTaskTopic: build.mutation<TaskResponse, { taskId: string }>({
      query: (body: { taskId: string }) => ({
        url: `v1/task/topic/${body.taskId}`,
        method: "DELETE",
      }),
      invalidatesTags: (_result, _err, req) => [
        { type: "v1/task/from-workspace/{workspaceName}/{taskTag}" },
        "v1/task/list/filter",
        "v1/workspace/activity/filter",
        "v1/task-analytics/{workspaceId}/team/{teamId}",
      ],
    }),
  }),
});

export const { useUpdateTaskTopicMutation, useRemoveTaskTopicMutation } = taskTopicApi;

export const {
  endpoints: { updateTaskTopic, removeTaskTopic },
} = taskTopicApi;
