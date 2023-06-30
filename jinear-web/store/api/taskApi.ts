import { TaskInitializeRequest, TaskResponse } from "@/model/be/jinear-core";
import { api } from "./api";

export const taskApi = api.injectEndpoints({
  endpoints: (build) => ({
    initializeTask: build.mutation<TaskResponse, TaskInitializeRequest>({
      query: (body: TaskInitializeRequest) => ({
        url: `v1/task`,
        method: "POST",
        body,
      }),
      invalidatesTags: [
        "team-task-list",
        "workspace-task-list",
        "workplace-task-with-name-and-tag",
        "team-workflow-task-list",
        "team-topic-task-list",
        "team-all-task-list",
        "task-board-entry-listing",
      ],
    }),
    //
    retrieveWithWorkspaceNameAndTeamTagNo: build.query<
      TaskResponse,
      {
        workspaceName: string;
        taskTag: string;
      }
    >({
      query: (req: { workspaceName: string; taskTag: string }) => `v1/task/from-workspace/${req.workspaceName}/${req.taskTag}`,
      providesTags: (_result, _err, req) => [
        {
          type: "workplace-task-with-name-and-tag",
          id: `${req.workspaceName}/${req.taskTag}`,
        },
      ],
    }),
  }),
});

export const { useInitializeTaskMutation, useRetrieveWithWorkspaceNameAndTeamTagNoQuery } = taskApi;

export const {
  endpoints: { initializeTask, retrieveWithWorkspaceNameAndTeamTagNo },
} = taskApi;
