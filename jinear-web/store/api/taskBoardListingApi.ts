import { TaskAndTaskBoardRelationResponse, TaskBoardListingPaginatedResponse } from "@/model/be/jinear-core";
import { api } from "./api";

interface ITaskBoardListingRequest {
  workspaceId: string;
  teamId: string;
  page?: number;
}

interface ITaskBoardFilterRequest extends ITaskBoardListingRequest {
  name: string;
}

interface IRetrieveTaskAndTaskBoardsRelation {
  taskId: string;
  filterRecentsByName: string;
}
export const taskBoardListingApi = api.injectEndpoints({
  endpoints: (build) => ({
    retrieveAllTaskBoards: build.query<TaskBoardListingPaginatedResponse, ITaskBoardListingRequest>({
      query: ({ workspaceId, teamId, page = 0 }: ITaskBoardListingRequest) =>
        `v1/task-board/list/${workspaceId}/team/${teamId}?page=${page}`,
      providesTags: (_result, _err, req) => [
        {
          type: "v1/task-board/list/{workspaceId}/team/{teamId}",
          id: `${req.workspaceId}-${req.teamId}-${req.page}`,
        },
      ],
    }),
    //
    filterTaskBoards: build.query<TaskBoardListingPaginatedResponse, ITaskBoardFilterRequest>({
      query: ({ workspaceId, teamId, name = "", page = 0 }: ITaskBoardFilterRequest) =>
        `v1/task-board/list/${workspaceId}/team/${teamId}/filter?name=${encodeURI(name)}&page=${page}`,
      providesTags: (_result, _err, req) => [
        {
          type: "v1/task-board/list/{workspaceId}/team/{teamId}/filter",
          id: `${req.workspaceId}-${req.teamId}-${req.page}`,
        },
      ],
    }),
    //
    retrieveTaskAndTaskBoardsRelation: build.query<TaskAndTaskBoardRelationResponse, IRetrieveTaskAndTaskBoardsRelation>({
      query: ({ taskId, filterRecentsByName = "" }: IRetrieveTaskAndTaskBoardsRelation) =>
        `v1/task-board/list/related-with-task/${taskId}?filterRecentsByName=${encodeURI(filterRecentsByName)}`,
      providesTags: (_result, _err, req) => [
        {
          type: "v1/task-board/list/related-with-task/{taskId}",
          id: `${req.taskId}`,
        },
      ],
    }),
    //
  }),
});

export const { useRetrieveAllTaskBoardsQuery, useFilterTaskBoardsQuery, useRetrieveTaskAndTaskBoardsRelationQuery } =
  taskBoardListingApi;

export const {
  endpoints: { retrieveAllTaskBoards, filterTaskBoards, retrieveTaskAndTaskBoardsRelation },
} = taskBoardListingApi;
