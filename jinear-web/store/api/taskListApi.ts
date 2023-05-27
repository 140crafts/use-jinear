import {
  BaseResponse,
  TaskListInitializeRequest,
  TaskListResponse,
  TaskListUpdateDueDateRequest,
  TaskListUpdateStateRequest,
  TaskListUpdateTitleRequest,
} from "@/model/be/jinear-core";
import { api } from "./api";

export const taskListApi = api.injectEndpoints({
  endpoints: (build) => ({
    initializeTaskList: build.mutation<TaskListResponse, TaskListInitializeRequest>({
      query: (body: TaskListInitializeRequest) => ({
        url: `v1/task-list`,
        method: "POST",
        body,
      }),
      invalidatesTags: ["task-list-listing"],
    }),
    //
    updateDueDate: build.mutation<BaseResponse, TaskListUpdateDueDateRequest>({
      query: (body: TaskListUpdateDueDateRequest) => ({
        url: `v1/task-list/update/due-date`,
        method: "PUT",
        body,
      }),
      invalidatesTags: ["task-list-listing"],
    }),
    //
    updateTitle: build.mutation<BaseResponse, TaskListUpdateTitleRequest>({
      query: (body: TaskListUpdateTitleRequest) => ({
        url: `v1/task-list/update/title`,
        method: "PUT",
        body,
      }),
      invalidatesTags: ["task-list-listing"],
    }),
    //
    updateState: build.mutation<BaseResponse, TaskListUpdateStateRequest>({
      query: (body: TaskListUpdateStateRequest) => ({
        url: `v1/task-list/update/state`,
        method: "PUT",
        body,
      }),
      invalidatesTags: ["task-list-listing"],
    }),
    //
  }),
});

export const { useInitializeTaskListMutation, useUpdateDueDateMutation, useUpdateTitleMutation, useUpdateStateMutation } =
  taskListApi;

export const {
  endpoints: { initializeTaskList, updateDueDate, updateTitle, updateState },
} = taskListApi;
