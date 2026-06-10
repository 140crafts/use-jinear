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
        "v1/task/from-workspace/{workspaceName}/{taskTag}",
        "v1/task-board/entry/from-task-board/{taskBoardId}",
        "v1/task/list/filter",
        "v1/calendar/event/filter",
        "v1/workspace/activity/filter",
        "v1/task-analytics/{workspaceId}/team/{teamId}",
        "v1/task/search/${workspaceId}",
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
          type: "v1/task/from-workspace/{workspaceName}/{taskTag}",
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
