import { BaseResponse, TaskBoardEntryInitializeRequest, TaskBoardEntryPaginatedResponse } from "@/model/be/jinear-core";
import { api } from "./api";

interface IRetrieveFromTaskBoardRequest {
  taskBoardId: string;
  page?: number;
}

export const taskBoardEntryApi = api.injectEndpoints({
  endpoints: (build) => ({
    initializeTaskBoardEntry: build.mutation<BaseResponse, TaskBoardEntryInitializeRequest>({
      query: (body: TaskBoardEntryInitializeRequest) => ({
        url: `v1/task-board/entry`,
        method: "POST",
        body,
      }),
      invalidatesTags: (_result, _err, req) => [{ type: "task-board-entry-listing", id: req.taskBoardId }],
    }),
    //
    deleteTaskBoardEntry: build.mutation<BaseResponse, { taskBoardEntryId: string; taskBoardId?: string }>({
      query: (req: { taskBoardEntryId: string }) => ({
        url: `v1/task-board/entry/${req.taskBoardEntryId}`,
        method: "DELETE",
      }),
      invalidatesTags: (_result, _err, req) => [{ type: "task-board-entry-listing", id: req.taskBoardId }],
    }),
    //
    changeOrder: build.mutation<BaseResponse, { taskBoardEntryId: string; newOrder: number; taskBoardId?: string }>({
      query: (req: { taskBoardEntryId: string; newOrder: number }) => ({
        url: `v1/task-board/entry/${req.taskBoardEntryId}/change-order?newOrder=${req.newOrder}`,
        method: "PUT",
      }),
      invalidatesTags: (_result, _err, req) => [{ type: "task-board-entry-listing", id: req.taskBoardId }],
    }),
    //
    retrieveFromTaskBoard: build.query<TaskBoardEntryPaginatedResponse, IRetrieveFromTaskBoardRequest>({
      query: ({ taskBoardId, page = 0 }: IRetrieveFromTaskBoardRequest) =>
        `v1/task-board/entry/from-task-board/${taskBoardId}?page=${page}`,
      providesTags: (_result, _err, req) => [
        {
          type: "task-board-entry-listing",
          id: `${req.taskBoardId}`,
        },
      ],
    }),
    //
  }),
});

export const {
  useInitializeTaskBoardEntryMutation,
  useDeleteTaskBoardEntryMutation,
  useChangeOrderMutation,
  useRetrieveFromTaskBoardQuery,
} = taskBoardEntryApi;

export const {
  endpoints: { initializeTaskBoardEntry, deleteTaskBoardEntry, changeOrder, retrieveFromTaskBoard },
} = taskBoardEntryApi;
