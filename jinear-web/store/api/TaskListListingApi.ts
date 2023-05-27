import { TaskListListingPaginatedResponse } from "@/model/be/jinear-core";
import { api } from "./api";

interface ITaskListListingRequest {
  workspaceId: string;
  teamId: string;
  page?: number;
}

export const taskListListingApi = api.injectEndpoints({
  endpoints: (build) => ({
    retrieveAllTaskLists: build.query<TaskListListingPaginatedResponse, ITaskListListingRequest>({
      query: ({ workspaceId, teamId, page = 0 }: ITaskListListingRequest) =>
        `v1/task-list/list/${workspaceId}/team/${teamId}?page=${page}`,
      providesTags: (_result, _err, req) => [
        {
          type: "task-list-listing",
          id: `${req.workspaceId}-${req.teamId}-${req.page}`,
        },
      ],
    }),
  }),
  //
  //
});

export const { useRetrieveAllTaskListsQuery } = taskListListingApi;

export const {
  endpoints: { retrieveAllTaskLists },
} = taskListListingApi;
