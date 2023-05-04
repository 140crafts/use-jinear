import {
  RetrieveIntersectingTasksFromTeamRequest,
  RetrieveIntersectingTasksFromWorkspaceRequest,
  TaskListingPaginatedResponse,
  TaskListingResponse,
} from "@/model/be/jinear-core";
import { api } from "./api";

export const taskListingApi = api.injectEndpoints({
  endpoints: (build) => ({
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
    retrieveFromWorkflowStatus: build.query<
      TaskListingPaginatedResponse,
      {
        workspaceId: string;
        teamId: string;
        workflowStatusId: string;
        page: number;
      }
    >({
      query: (req: { workspaceId: string; teamId: string; workflowStatusId: string; page: number }) => {
        const page = req.page ? req.page : 0;
        return `v1/task/list/${req.workspaceId}/team/${req.teamId}/with-workflow/${req.workflowStatusId}?page=${page}`;
      },
      providesTags: (_result, _err, req) => [
        {
          type: "team-workflow-task-list",
          id: `${JSON.stringify(req)}`,
        },
      ],
    }),
    //
    retrieveFromTopic: build.query<
      TaskListingPaginatedResponse,
      {
        workspaceId: string;
        teamId: string;
        topicTag: string;
        page: number;
      }
    >({
      query: (req: { workspaceId: string; teamId: string; topicTag: string; page: number }) => {
        const page = req.page ? req.page : 0;
        return `v1/task/list/${req.workspaceId}/team/${req.teamId}/with-topic/${req.topicTag}?page=${page}`;
      },
      providesTags: (_result, _err, req) => [
        {
          type: "team-topic-task-list",
          id: `${JSON.stringify(req)}`,
        },
      ],
    }),
    //
    retrieveWithAssignee: build.query<
      TaskListingPaginatedResponse,
      {
        workspaceId: string;
        teamId: string;
        assigneeId: string;
        page: number;
      }
    >({
      query: (req: { workspaceId: string; teamId: string; assigneeId: string; page: number }) => {
        const page = req.page ? req.page : 0;
        return `v1/task/list/${req.workspaceId}/team/${req.teamId}/with-assignee/${req.assigneeId}?page=${page}`;
      },
      providesTags: (_result, _err, req) => [
        {
          type: "task-list-with-assignee",
          id: `${JSON.stringify(req)}`,
        },
      ],
    }),
    //
    retrieveAssignedToCurrentAccount: build.query<
      TaskListingPaginatedResponse,
      {
        workspaceId: string;
        page: number;
      }
    >({
      query: (req: { workspaceId: string; page: number }) => {
        const page = req.page ? req.page : 0;
        return `v1/task/list/${req.workspaceId}/assigned-to-me?page=${page}`;
      },
      providesTags: (_result, _err, req) => [
        {
          type: "task-list-assigned-to-current-account",
          id: `${JSON.stringify(req)}`,
        },
      ],
    }),
    //
    retrieveAllTasks: build.query<
      TaskListingPaginatedResponse,
      {
        workspaceId: string;
        teamId: string;
        page: number;
      }
    >({
      query: (req: { workspaceId: string; teamId: string; page: number }) => {
        const page = req.page ? req.page : 0;
        return `v1/task/list/${req.workspaceId}/team/${req.teamId}?page=${page}`;
      },
      providesTags: (_result, _err, req) => [
        {
          type: "team-all-task-list",
          id: `${JSON.stringify(req)}`,
        },
      ],
    }),
  }),
});

export const {
  useRetrieveAllIntersectingTasksQuery,
  useRetrieveAllIntersectingTasksFromTeamQuery,
  useRetrieveFromWorkflowStatusQuery,
  useRetrieveFromTopicQuery,
  useRetrieveAllTasksQuery,
  useRetrieveWithAssigneeQuery,
  useRetrieveAssignedToCurrentAccountQuery,
} = taskListingApi;

export const {
  endpoints: {
    retrieveAllIntersectingTasks,
    retrieveAllIntersectingTasksFromTeam,
    retrieveFromWorkflowStatus,
    retrieveAllTasks,
    retrieveFromTopic,
    retrieveWithAssignee,
    retrieveAssignedToCurrentAccount,
  },
} = taskListingApi;
