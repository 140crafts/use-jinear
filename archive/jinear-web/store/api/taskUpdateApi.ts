import {
  TaskAssigneeUpdateRequest,
  TaskDateUpdateRequest,
  TaskProjectAndMilestoneUpdateRequest,
  TaskResponse,
  TaskUpdateDescriptionRequest,
  TaskUpdateTitleRequest
} from "@/model/be/jinear-core";
import { api } from "./api";
import { calendarEventApi } from "./calendarEventApi";
import { taskListingApi } from "./taskListingApi";

export const taskUpdateApi = api.injectEndpoints({
  endpoints: (build) => ({
    updateTaskDescription: build.mutation<TaskResponse, { taskId: string; body: TaskUpdateDescriptionRequest }>({
      query: (req: { taskId: string; body: TaskUpdateDescriptionRequest }) => ({
        url: `v1/task/update/${req.taskId}/description`,
        method: "PUT",
        body: req.body
      }),
      invalidatesTags: (_result, _err, req) => [
        { type: "v1/task/from-workspace/{workspaceName}/{taskTag}" },
        { type: "v1/workspace/activity/filter" },
        { type: "v1/task/list/filter" },
        { type: "v1/calendar/event/filter" },
        { type: "v1/task-analytics/{workspaceId}/team/{teamId}" }
      ]
    }),
    //
    updateTaskTitle: build.mutation<TaskResponse, { taskId: string; body: TaskUpdateTitleRequest }>({
      query: (req: { taskId: string; body: TaskUpdateTitleRequest }) => ({
        url: `v1/task/update/${req.taskId}/title`,
        method: "PUT",
        body: req.body
      }),
      invalidatesTags: (_result, _err, req) => [
        { type: "v1/task/from-workspace/{workspaceName}/{taskTag}" },
        { type: "v1/workspace/activity/filter" },
        { type: "v1/task/list/filter" },
        { type: "v1/calendar/event/filter" },
        { type: "v1/task-analytics/{workspaceId}/team/{teamId}" }
      ]
    }),
    //
    updateTaskDates: build.mutation<TaskResponse, { taskId: string; body: TaskDateUpdateRequest }>({
      query: (req: { taskId: string; body: TaskDateUpdateRequest }) => ({
        url: `v1/task/update/${req.taskId}/dates`,
        method: "PUT",
        body: req.body
      }),
      onQueryStarted(req, { dispatch, queryFulfilled, getState }) {
        //cgds-448 migh need later
        const invalidatedTaskListFilterCaches = taskListingApi.util.selectInvalidatedBy(getState(), ["v1/task/list/filter"]);
        const invalidatedEventFilterCaches = calendarEventApi.util.selectInvalidatedBy(getState(), ["v1/calendar/event/filter"]);
        invalidatedEventFilterCaches
          .filter((cache) => cache.endpointName == "filterCalendarEvents")
          .forEach((cache) => {
            dispatch(
              calendarEventApi.util.updateQueryData("filterCalendarEvents", cache.originalArgs, (draft) => {
                draft.data
                  .filter((calendarEventDto) => calendarEventDto.calendarEventSourceType == "TASK")
                  .filter((calendarEventDto) => calendarEventDto.relatedTask?.taskId == req.taskId)
                  .forEach((calendarEventDto) => {
                    if (req.body.assignedDate) {
                      calendarEventDto.assignedDate = req.body.assignedDate;
                      if (calendarEventDto.relatedTask) {
                        calendarEventDto.relatedTask.assignedDate = req.body.assignedDate;
                      }
                    }
                    if (req.body.dueDate) {
                      calendarEventDto.dueDate = req.body.dueDate;
                      if (calendarEventDto.relatedTask) {
                        calendarEventDto.relatedTask.dueDate = req.body.dueDate;
                      }
                    }
                    if (req.body.hasPreciseAssignedDate) {
                      calendarEventDto.hasPreciseAssignedDate = req.body.hasPreciseAssignedDate;
                      if (calendarEventDto.relatedTask) {
                        calendarEventDto.relatedTask.hasPreciseAssignedDate = req.body.hasPreciseAssignedDate;
                      }
                    }
                    if (req.body.hasPreciseDueDate) {
                      calendarEventDto.hasPreciseDueDate = req.body.hasPreciseDueDate;
                      if (calendarEventDto.relatedTask) {
                        calendarEventDto.relatedTask.hasPreciseDueDate = req.body.hasPreciseDueDate;
                      }
                    }
                  });
                return draft;
              })
            );
          });
      },
      invalidatesTags: (_result, _err, req) => [
        { type: "v1/task/from-workspace/{workspaceName}/{taskTag}" },
        { type: "v1/workspace/activity/filter" },
        { type: "v1/task/reminder/job/{taskReminderId}/next" },
        { type: "v1/task/list/filter" },
        { type: "v1/calendar/event/filter" },
        { type: "v1/task-analytics/{workspaceId}/team/{teamId}" }
      ]
    }),
    //
    //
    updateTaskAssignee: build.mutation<TaskResponse, { taskId: string; body: TaskAssigneeUpdateRequest }>({
      query: (req: { taskId: string; body: TaskAssigneeUpdateRequest }) => ({
        url: `v1/task/update/${req.taskId}/assignee`,
        method: "PUT",
        body: req.body
      }),
      invalidatesTags: (_result, _err, req) => [
        { type: "v1/task/from-workspace/{workspaceName}/{taskTag}" },
        { type: "v1/workspace/activity/filter" },
        { type: "v1/task/list/filter" },
        { type: "v1/calendar/event/filter" },
        { type: "v1/task-analytics/{workspaceId}/team/{teamId}" }
      ]
    }),
    //
    updateTaskProjectAnMilestone: build.mutation<TaskResponse, { taskId: string; body: TaskProjectAndMilestoneUpdateRequest }>({
      query: (req: { taskId: string; body: TaskProjectAndMilestoneUpdateRequest }) => ({
        url: `v1/task/update/${req.taskId}/project-milestone`,
        method: "PUT",
        body: req.body
      }),
      invalidatesTags: (_result, _err, req) => [
        { type: "v1/task/from-workspace/{workspaceName}/{taskTag}" },
        { type: "v1/workspace/activity/filter" },
        { type: "v1/task/list/filter" },
        { type: "v1/calendar/event/filter" },
        { type: "v1/task-analytics/{workspaceId}/team/{teamId}" }
      ]
    }),
    //
  })
});

export const {
  useUpdateTaskDescriptionMutation,
  useUpdateTaskTitleMutation,
  useUpdateTaskDatesMutation,
  useUpdateTaskAssigneeMutation,
  useUpdateTaskProjectAnMilestoneMutation
} = taskUpdateApi;

export const {
  endpoints: {
    updateTaskDescription,
    updateTaskTitle,
    updateTaskDates,
    updateTaskAssignee,
    updateTaskProjectAnMilestone
  }
} = taskUpdateApi;
