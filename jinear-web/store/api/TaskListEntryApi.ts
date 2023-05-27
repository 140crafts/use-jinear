import { BaseResponse, TaskListEntryInitializeRequest, TaskListEntryPaginatedResponse } from "@/model/be/jinear-core";
import { api } from "./api";

interface IRetrieveFromTaskListRequest {
  taskListId: string;
  page?: number;
}

export const taskListEntryApi = api.injectEndpoints({
  endpoints: (build) => ({
    initializeTaskListEntry: build.mutation<BaseResponse, TaskListEntryInitializeRequest>({
      query: (body: TaskListEntryInitializeRequest) => ({
        url: `v1/task-list/entry`,
        method: "POST",
        body,
      }),
      invalidatesTags: (_result, _err, req) => [{ type: "task-list-entry-listing", id: req.taskListId }],
    }),
    //
    deleteTaskListEntry: build.mutation<BaseResponse, { taskListEntryId: string; taskListId?: string }>({
      query: (req: { taskListEntryId: string }) => ({
        url: `v1/task-list/entry/${req.taskListEntryId}`,
        method: "DELETE",
      }),
      invalidatesTags: (_result, _err, req) => [{ type: "task-list-entry-listing", id: req.taskListId }],
    }),
    //
    changeOrder: build.mutation<BaseResponse, { taskListEntryId: string; newOrder: number; taskListId?: string }>({
      query: (req: { taskListEntryId: string; newOrder: number }) => ({
        url: `v1/task-list/entry/${req.taskListEntryId}/change-order?newOrder=${req.newOrder}`,
        method: "PUT",
      }),
      invalidatesTags: (_result, _err, req) => [{ type: "task-list-entry-listing", id: req.taskListId }],
    }),
    //
    retrieveFromTaskList: build.query<TaskListEntryPaginatedResponse, IRetrieveFromTaskListRequest>({
      query: ({ taskListId, page = 0 }: IRetrieveFromTaskListRequest) =>
        `v1/task-list/entry/from-task-list/${taskListId}?page=${page}`,
      providesTags: (_result, _err, req) => [
        {
          type: "task-list-entry-listing",
          id: `${req.taskListId}`, //-${page}
        },
      ],
    }),
    //
  }),
});

export const {
  useInitializeTaskListEntryMutation,
  useDeleteTaskListEntryMutation,
  useChangeOrderMutation,
  useRetrieveFromTaskListQuery,
} = taskListEntryApi;

export const {
  endpoints: { initializeTaskListEntry, deleteTaskListEntry, changeOrder, retrieveFromTaskList },
} = taskListEntryApi;
