import { WorkspaceActivityListResponse } from "@/model/be/jinear-core";
import { api } from "./api";

export const workspaceActivityApi = api.injectEndpoints({
  endpoints: (build) => ({
    retrieveActivities: build.query<WorkspaceActivityListResponse, { workspaceId: string; page?: number }>({
      query: ({ workspaceId, page = 0 }) => `v1/workspace/activity/${workspaceId}?page=${page}`,
      providesTags: (_result, _err, { workspaceId, page }) => [
        {
          type: "workspace-activity-list",
          id: `${workspaceId}-${page}`,
        },
      ],
    }),
    retrieveActivitiesFromTeam: build.query<
      WorkspaceActivityListResponse,
      { workspaceId: string; teamId: string; page?: number }
    >({
      query: ({ workspaceId, teamId, page = 0 }) => `v1/workspace/activity/${workspaceId}/team/${teamId}?page=${page}`,
      providesTags: (_result, _err, { workspaceId, teamId, page }) => [
        {
          type: "workspace-team-activity-list",
          id: `${workspaceId}-${teamId}-${page}`,
        },
      ],
    }),
    retrieveActivitiesFromTask: build.query<
      WorkspaceActivityListResponse,
      { workspaceId: string; taskId: string; page?: number }
    >({
      query: ({ workspaceId, taskId, page = 0 }) => `v1/workspace/activity/${workspaceId}/task/${taskId}?page=${page}`,
      providesTags: (_result, _err, { workspaceId, taskId, page }) => [
        {
          type: "workspace-task-activity-list",
          id: `${workspaceId}-${taskId}-${page}`,
        },
      ],
    }),
  }),
});

export const { useRetrieveActivitiesQuery, useRetrieveActivitiesFromTeamQuery, useRetrieveActivitiesFromTaskQuery } =
  workspaceActivityApi;

export const {
  endpoints: { retrieveActivities, retrieveActivitiesFromTeam, retrieveActivitiesFromTask },
} = workspaceActivityApi;
