import {
  BaseResponse,
  TaskBoardEntryFilterRequest,
  TaskBoardEntryInitializeRequest,
  TaskBoardEntryPaginatedResponse
} from "@/model/be/jinear-core";
import { api } from "./api";

interface IRetrieveFromTaskBoardRequest {
  taskBoardId: string;
  page?: number;
}

interface IFilterFromTaskBoardRequest extends IRetrieveFromTaskBoardRequest {
  body: TaskBoardEntryFilterRequest;
}

export const taskBoardEntryApi = api.injectEndpoints({
  endpoints: (build) => ({
    initializeTaskBoardEntry: build.mutation<BaseResponse, TaskBoardEntryInitializeRequest>({
      query: (body: TaskBoardEntryInitializeRequest) => ({
        url: `v1/task-board/entry`,
        method: "POST",
        body
      }),
      invalidatesTags: (_result, _err, req) => [
        "v1/task-board/entry/from-task-board/{taskBoardId}",
        "v1/task-board/entry/from-task-board/{taskBoardId}/filter",
        "v1/task-board/list/related-with-task/{taskId}",
        "v1/workspace/activity/filter"
      ]
    }),
    //
    deleteTaskBoardEntry: build.mutation<BaseResponse, { taskBoardEntryId: string; taskBoardId?: string }>({
      query: (req: { taskBoardEntryId: string }) => ({
        url: `v1/task-board/entry/${req.taskBoardEntryId}`,
        method: "DELETE"
      }),
      invalidatesTags: (_result, _err, req) => [
        "v1/task-board/entry/from-task-board/{taskBoardId}",
        "v1/task-board/entry/from-task-board/{taskBoardId}/filter",
        "v1/task-board/list/related-with-task/{taskId}",
        "v1/workspace/activity/filter"
      ]
    }),
    //
    changeBoardEntryOrder: build.mutation<BaseResponse, {
      taskBoardEntryId: string;
      newOrder: number;
      taskBoardId?: string
    }>({
      query: (req: { taskBoardEntryId: string; newOrder: number }) => ({
        url: `v1/task-board/entry/${req.taskBoardEntryId}/change-order?newOrder=${req.newOrder}`,
        method: "PUT"
      }),
      invalidatesTags: (_result, _err, req) =>
        [
          "v1/task-board/entry/from-task-board/{taskBoardId}",
          "v1/task-board/entry/from-task-board/{taskBoardId}/filter"
        ]
    }),
    //
    changeFilteredBoardEntryOrder: build.mutation<BaseResponse, {
      taskBoardEntryId: string;
      taskBoardEntryIdBefore?: string;
      taskBoardEntryIdAfter?: string;
    }>({
      query: ({ taskBoardEntryId, taskBoardEntryIdBefore = "", taskBoardEntryIdAfter = "" }: {
        taskBoardEntryId: string;
        taskBoardEntryIdBefore?: string,
        taskBoardEntryIdAfter?: string
      }) => ({
        url: `v1/task-board/entry/${taskBoardEntryId}/change-filtered-order?taskBoardEntryIdBefore=${taskBoardEntryIdBefore}&taskBoardEntryIdAfter=${taskBoardEntryIdAfter}`,
        method: "PUT"
      }),
      invalidatesTags: (_result, _err, req) =>
        [
          "v1/task-board/entry/from-task-board/{taskBoardId}",
          "v1/task-board/entry/from-task-board/{taskBoardId}/filter"
        ]
    }),
    //
    retrieveFromTaskBoard: build.query<TaskBoardEntryPaginatedResponse, IRetrieveFromTaskBoardRequest>({
      query: ({ taskBoardId, page = 0 }: IRetrieveFromTaskBoardRequest) =>
        `v1/task-board/entry/from-task-board/${taskBoardId}?page=${page}`,
      providesTags: (_result, _err, req) => [
        {
          type: "v1/task-board/entry/from-task-board/{taskBoardId}",
          id: `${req.taskBoardId}-${req.page}`
        }
      ]
    }),
    //
    filterFromTaskBoard: build.query<TaskBoardEntryPaginatedResponse, IFilterFromTaskBoardRequest>({
      query: ({
                taskBoardId,
                page = 0,
                body
              }: IFilterFromTaskBoardRequest) => ({
        url: `v1/task-board/entry/from-task-board/${taskBoardId}/filter?page=${page}`,
        method: "POST",
        body: body
      }),
      providesTags: (_result, _err, req) => [
        {
          type: "v1/task-board/entry/from-task-board/{taskBoardId}/filter",
          id: `${req.taskBoardId}-${req.page}`
        }
      ]
    })
    //
  })
});

export const {
  useInitializeTaskBoardEntryMutation,
  useDeleteTaskBoardEntryMutation,
  useChangeBoardEntryOrderMutation,
  useChangeFilteredBoardEntryOrderMutation,
  useRetrieveFromTaskBoardQuery,
  useFilterFromTaskBoardQuery
} = taskBoardEntryApi;

export const {
  endpoints: {
    initializeTaskBoardEntry,
    deleteTaskBoardEntry,
    changeBoardEntryOrder,
    changeFilteredBoardEntryOrder,
    retrieveFromTaskBoard,
    filterFromTaskBoard
  }
} = taskBoardEntryApi;
