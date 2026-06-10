import {
  BaseResponse,
  TaskBoardInitializeRequest,
  TaskBoardResponse, TaskBoardUpdateColorRequest,
  TaskBoardUpdateDueDateRequest,
  TaskBoardUpdateStateRequest,
  TaskBoardUpdateTitleRequest
} from "@/model/be/jinear-core";
import { api } from "./api";

export const taskBoardApi = api.injectEndpoints({
  endpoints: (build) => ({
    initializeTaskBoard: build.mutation<TaskBoardResponse, TaskBoardInitializeRequest>({
      query: (body: TaskBoardInitializeRequest) => ({
        url: `v1/task-board`,
        method: "POST",
        body
      }),
      invalidatesTags: [
        "v1/task-board/list/{workspaceId}/team/{teamId}",
        "v1/task-board",
        "v1/task-board/list/{workspaceId}/team/{teamId}/filter",
        "v1/task-board/list/related-with-task/{taskId}",
        "v1/workspace/activity/filter"
      ]
    }),
    //
    updateDueDate: build.mutation<BaseResponse, TaskBoardUpdateDueDateRequest>({
      query: (body: TaskBoardUpdateDueDateRequest) => ({
        url: `v1/task-board/update/due-date`,
        method: "PUT",
        body
      }),
      invalidatesTags: [
        "v1/task-board/list/{workspaceId}/team/{teamId}",
        "v1/task-board",
        "v1/task-board/list/{workspaceId}/team/{teamId}/filter",
        "v1/workspace/activity/filter"
      ]
    }),
    //
    updateTitle: build.mutation<BaseResponse, TaskBoardUpdateTitleRequest>({
      query: (body: TaskBoardUpdateTitleRequest) => ({
        url: `v1/task-board/update/title`,
        method: "PUT",
        body
      }),
      invalidatesTags: [
        "v1/task-board/list/{workspaceId}/team/{teamId}",
        "v1/task-board",
        "v1/task-board/list/{workspaceId}/team/{teamId}/filter",
        "v1/task-board/list/related-with-task/{taskId}",
        "v1/workspace/activity/filter"
      ]
    }),
    //
    updateState: build.mutation<BaseResponse, TaskBoardUpdateStateRequest>({
      query: (body: TaskBoardUpdateStateRequest) => ({
        url: `v1/task-board/update/state`,
        method: "PUT",
        body
      }),
      invalidatesTags: [
        "v1/task-board/list/{workspaceId}/team/{teamId}",
        "v1/task-board",
        "v1/task-board/list/{workspaceId}/team/{teamId}/filter",
        "v1/workspace/activity/filter"
      ]
    }),
    //
    updateColor: build.mutation<BaseResponse, TaskBoardUpdateColorRequest>({
      query: (body: TaskBoardUpdateColorRequest) => ({
        url: `v1/task-board/update/color`,
        method: "PUT",
        body
      }),
      invalidatesTags: [
        "v1/task-board/list/{workspaceId}/team/{teamId}",
        "v1/task-board",
        "v1/task-board/list/{workspaceId}/team/{teamId}/filter",
        "v1/workspace/activity/filter",
        "v1/calendar/event/filter",
        "v1/task/list/filter"
      ]
    })
    //
  })
});

export const {
  useInitializeTaskBoardMutation,
  useUpdateDueDateMutation,
  useUpdateTitleMutation,
  useUpdateStateMutation,
  useUpdateColorMutation
} =
  taskBoardApi;

export const {
  endpoints: { initializeTaskBoard, updateDueDate, updateTitle, updateState, updateColor }
} = taskBoardApi;
