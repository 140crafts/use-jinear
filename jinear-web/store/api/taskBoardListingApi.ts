import { TaskBoardListingPaginatedResponse } from "@/model/be/jinear-core";
import { api } from "./api";

interface ITaskBoardListingRequest {
  workspaceId: string;
  teamId: string;
  page?: number;
}

export const taskBoardListingApi = api.injectEndpoints({
  endpoints: (build) => ({
    retrieveAllTaskBoards: build.query<TaskBoardListingPaginatedResponse, ITaskBoardListingRequest>({
      query: ({ workspaceId, teamId, page = 0 }: ITaskBoardListingRequest) =>
        `v1/task-board/list/${workspaceId}/team/${teamId}?page=${page}`,
      providesTags: (_result, _err, req) => [
        {
          type: "task-board-listing",
          id: `${req.workspaceId}-${req.teamId}-${req.page}`,
        },
      ],
    }),
  }),
  //
  //
});

export const { useRetrieveAllTaskBoardsQuery } = taskBoardListingApi;

export const {
  endpoints: { retrieveAllTaskBoards },
} = taskBoardListingApi;
