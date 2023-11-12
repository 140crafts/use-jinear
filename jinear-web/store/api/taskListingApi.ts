import {
  RetrieveIntersectingTasksFromTeamRequest,
  RetrieveIntersectingTasksFromWorkspaceRequest,
  TaskFilterRequest,
  TaskListingPaginatedResponse,
  TaskListingResponse,
} from "@/model/be/jinear-core";
import { api } from "./api";

export const taskListingApi = api.injectEndpoints({
  endpoints: (build) => ({
    filterTasks: build.query<TaskListingPaginatedResponse, TaskFilterRequest>({
      query: (req) => ({ url: `v1/task/list/filter`, method: "POST", body: req }),
      providesTags: (_result, _err, req) => [
        {
          type: "task-listing-filter",
          id: `${JSON.stringify(req)}`,
        },
      ],
    }),
    //
    retrieveAllIntersectingTasks: build.query<TaskListingResponse, RetrieveIntersectingTasksFromWorkspaceRequest>({
      query: (req) => {
        const start = new Date(req.timespanStart).toISOString();
        const end = new Date(req.timespanEnd).toISOString();
        return `v1/task/list/${req.workspaceId}/intersecting/${start}/${end}`;
      },
      providesTags: (_result, _err, req) => [
        {
          type: "workspace-task-list",
          id: `${JSON.stringify(req)}`,
        },
      ],
    }),
    //
    retrieveAllIntersectingTasksFromTeam: build.query<TaskListingResponse, RetrieveIntersectingTasksFromTeamRequest>({
      query: (req) => {
        const { workspaceId, teamId } = req;
        const start = new Date(req.timespanStart).toISOString();
        const end = new Date(req.timespanEnd).toISOString();
        return `v1/task/list/${workspaceId}/team/${teamId}/intersecting/${start}/${end}`;
      },
      providesTags: (_result, _err, req) => [
        {
          type: "team-task-list",
          id: `${JSON.stringify(req)}`,
        },
      ],
    }),
    //
  }),
});

export const { useFilterTasksQuery, useRetrieveAllIntersectingTasksQuery, useRetrieveAllIntersectingTasksFromTeamQuery } =
  taskListingApi;

export const {
  endpoints: { filterTasks, retrieveAllIntersectingTasks, retrieveAllIntersectingTasksFromTeam },
} = taskListingApi;
