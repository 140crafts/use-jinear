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
      invalidatesTags: (_result, _err, req) => [
        "v1/task-board/entry/from-task-board/{taskBoardId}",
        "v1/task-board/list/related-with-task/{taskId}",
        "v1/workspace/activity/filter",
      ],
    }),
    //
    deleteTaskBoardEntry: build.mutation<BaseResponse, { taskBoardEntryId: string; taskBoardId?: string }>({
      query: (req: { taskBoardEntryId: string }) => ({
        url: `v1/task-board/entry/${req.taskBoardEntryId}`,
        method: "DELETE",
      }),
      invalidatesTags: (_result, _err, req) => [
        "v1/task-board/entry/from-task-board/{taskBoardId}",
        "v1/task-board/list/related-with-task/{taskId}",
        "v1/workspace/activity/filter",
      ],
    }),
    //
    changeBoardEntryOrder: build.mutation<BaseResponse, { taskBoardEntryId: string; newOrder: number; taskBoardId?: string }>({
      query: (req: { taskBoardEntryId: string; newOrder: number }) => ({
        url: `v1/task-board/entry/${req.taskBoardEntryId}/change-order?newOrder=${req.newOrder}`,
        method: "PUT",
      }),
      invalidatesTags: (_result, _err, req) => ["v1/task-board/entry/from-task-board/{taskBoardId}"],
    }),
    //
    retrieveFromTaskBoard: build.query<TaskBoardEntryPaginatedResponse, IRetrieveFromTaskBoardRequest>({
      query: ({ taskBoardId, page = 0 }: IRetrieveFromTaskBoardRequest) =>
        `v1/task-board/entry/from-task-board/${taskBoardId}?page=${page}`,
      providesTags: (_result, _err, req) => [
        {
          type: "v1/task-board/entry/from-task-board/{taskBoardId}",
          id: `${req.taskBoardId}-${req.page}`,
        },
      ],
    }),
    //
  }),
});

export const {
  useInitializeTaskBoardEntryMutation,
  useDeleteTaskBoardEntryMutation,
  useChangeBoardEntryOrderMutation,
  useRetrieveFromTaskBoardQuery,
} = taskBoardEntryApi;

export const {
  endpoints: { initializeTaskBoardEntry, deleteTaskBoardEntry, changeBoardEntryOrder, retrieveFromTaskBoard },
} = taskBoardEntryApi;
