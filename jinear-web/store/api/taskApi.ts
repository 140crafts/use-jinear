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
      invalidatesTags: ["team-task-list", "workplace-task-with-name-and-tag", "team-workflow-task-list"],
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
///from-workspace/{workspaceName}/{teamTag}-{tagNo}
export const { useInitializeTaskMutation, useRetrieveWithWorkspaceNameAndTeamTagNoQuery } = taskApi;

export const {
  endpoints: { initializeTask, retrieveWithWorkspaceNameAndTeamTagNo },
} = taskApi;
