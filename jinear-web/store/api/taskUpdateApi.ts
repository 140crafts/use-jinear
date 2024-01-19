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
        { type: "workspace-activity-list" },
        { type: "workspace-team-activity-list" },
        { type: "workspace-task-activity-list" },
        { type: "team-task-list" },
        { type: "task-listing-filter" },
        { type: "workspace-activity-filtered-list" },
        { type: "v1/task-analytics/{workspaceId}/team/{teamId}" },
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
        { type: "workspace-activity-list" },
        { type: "workspace-team-activity-list" },
        { type: "workspace-task-activity-list" },
        { type: "task-listing-filter" },
        { type: "workspace-activity-filtered-list" },
        { type: "v1/task-analytics/{workspaceId}/team/{teamId}" },
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
        { type: "workspace-activity-list" },
        { type: "workspace-team-activity-list" },
        { type: "workspace-task-activity-list" },
        { type: "reminder-next-job" },
        { type: "task-listing-filter" },
        { type: "workspace-activity-filtered-list" },
        { type: "v1/task-analytics/{workspaceId}/team/{teamId}" },
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
        { type: "workspace-activity-list" },
        { type: "workspace-team-activity-list" },
        { type: "workspace-task-activity-list" },
        { type: "task-listing-filter" },
        { type: "workspace-activity-filtered-list" },
        { type: "v1/task-analytics/{workspaceId}/team/{teamId}" },
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
