import {
  TaskAssigneeUpdateRequest,
  TaskDateUpdateRequest,
  TaskResponse,
  TaskUpdateDescriptionRequest,
  TaskUpdateTitleRequest,
} from "@/model/be/jinear-core";
import { api } from "./api";

export const taskUpdateApi = api.injectEndpoints({
  endpoints: (build) => ({
    updateTaskDescription: build.mutation<TaskResponse, { taskId: string; body: TaskUpdateDescriptionRequest }>({
      query: (req: { taskId: string; body: TaskUpdateDescriptionRequest }) => ({
        url: `v1/task/update/${req.taskId}/description`,
        method: "PUT",
        body: req.body,
      }),
      invalidatesTags: (_result, _err, req) => [
        { type: "team-task-list" },
        { type: "workspace-task-list" },
        { type: "workplace-task-with-name-and-tag" },
        { type: "retrieve-task-activity", id: req.taskId },
        { type: "team-task-list" },
      ],
    }),
    //
    updateTaskTitle: build.mutation<TaskResponse, { taskId: string; body: TaskUpdateTitleRequest }>({
      query: (req: { taskId: string; body: TaskUpdateTitleRequest }) => ({
        url: `v1/task/update/${req.taskId}/title`,
        method: "PUT",
        body: req.body,
      }),
      invalidatesTags: (_result, _err, req) => [
        { type: "team-task-list" },
        { type: "workspace-task-list" },
        { type: "workplace-task-with-name-and-tag" },
        { type: "retrieve-task-activity", id: req.taskId },
      ],
    }),
    //
    updateTaskDates: build.mutation<TaskResponse, { taskId: string; body: TaskDateUpdateRequest }>({
      query: (req: { taskId: string; body: TaskDateUpdateRequest }) => ({
        url: `v1/task/update/${req.taskId}/dates`,
        method: "PUT",
        body: req.body,
      }),
      invalidatesTags: (_result, _err, req) => [
        { type: "team-task-list" },
        { type: "workspace-task-list" },
        { type: "workplace-task-with-name-and-tag" },
        { type: "retrieve-task-activity", id: req.taskId },
        { type: "reminder-next-job" },
      ],
    }),
    //
    //
    updateTaskAssignee: build.mutation<TaskResponse, { taskId: string; body: TaskAssigneeUpdateRequest }>({
      query: (req: { taskId: string; body: TaskAssigneeUpdateRequest }) => ({
        url: `v1/task/update/${req.taskId}/assignee`,
        method: "PUT",
        body: req.body,
      }),
      invalidatesTags: (_result, _err, req) => [
        { type: "team-task-list" },
        { type: "workspace-task-list" },
        { type: "workplace-task-with-name-and-tag" },
        { type: "task-list-with-assignee" },
        { type: "task-list-assigned-to-current-account" },
        { type: "retrieve-task-activity", id: req.taskId },
      ],
    }),
    //
  }),
});

export const {
  useUpdateTaskDescriptionMutation,
  useUpdateTaskTitleMutation,
  useUpdateTaskDatesMutation,
  useUpdateTaskAssigneeMutation,
} = taskUpdateApi;

export const {
  endpoints: { updateTaskDescription, updateTaskTitle, updateTaskDates, updateTaskAssignee },
} = taskUpdateApi;
