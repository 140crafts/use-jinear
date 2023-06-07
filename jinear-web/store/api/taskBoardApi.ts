import {
  BaseResponse,
  TaskBoardInitializeRequest,
  TaskBoardResponse,
  TaskBoardUpdateDueDateRequest,
  TaskBoardUpdateStateRequest,
  TaskBoardUpdateTitleRequest,
} from "@/model/be/jinear-core";
import { api } from "./api";

export const taskBoardApi = api.injectEndpoints({
  endpoints: (build) => ({
    initializeTaskBoard: build.mutation<TaskBoardResponse, TaskBoardInitializeRequest>({
      query: (body: TaskBoardInitializeRequest) => ({
        url: `v1/task-board`,
        method: "POST",
        body,
      }),
      invalidatesTags: [
        "task-board-listing",
        "task-board-retrieve",
        "task-board-listing",
        "retrieve-task-and-task-boards-relation",
      ],
    }),
    //
    updateDueDate: build.mutation<BaseResponse, TaskBoardUpdateDueDateRequest>({
      query: (body: TaskBoardUpdateDueDateRequest) => ({
        url: `v1/task-board/update/due-date`,
        method: "PUT",
        body,
      }),
      invalidatesTags: ["task-board-listing", "task-board-retrieve", "task-board-listing"],
    }),
    //
    updateTitle: build.mutation<BaseResponse, TaskBoardUpdateTitleRequest>({
      query: (body: TaskBoardUpdateTitleRequest) => ({
        url: `v1/task-board/update/title`,
        method: "PUT",
        body,
      }),
      invalidatesTags: [
        "task-board-listing",
        "task-board-retrieve",
        "task-board-listing",
        "retrieve-task-and-task-boards-relation",
      ],
    }),
    //
    updateState: build.mutation<BaseResponse, TaskBoardUpdateStateRequest>({
      query: (body: TaskBoardUpdateStateRequest) => ({
        url: `v1/task-board/update/state`,
        method: "PUT",
        body,
      }),
      invalidatesTags: ["task-board-listing", "task-board-retrieve", "task-board-listing"],
    }),
    //
  }),
});

export const { useInitializeTaskBoardMutation, useUpdateDueDateMutation, useUpdateTitleMutation, useUpdateStateMutation } =
  taskBoardApi;

export const {
  endpoints: { initializeTaskBoard, updateDueDate, updateTitle, updateState },
} = taskBoardApi;
