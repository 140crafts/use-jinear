import {
  TaskListingPaginatedResponse,
  TaskListingResponse,
  TaskRetrieveIntersectingRequest,
} from "@/model/be/jinear-core";
import { api } from "./api";

export const taskListingApi = api.injectEndpoints({
  endpoints: (build) => ({
    retrieveAllIntersectingTasks: build.query<
      TaskListingResponse,
      TaskRetrieveIntersectingRequest
    >({
      query: (req) => {
        const { workspaceId, teamId } = req;
        const start = new Date(req.timespanStart).toISOString();
        const end = new Date(req.timespanEnd).toISOString();
        return `v1/task/list/${workspaceId}/${teamId}/intersecting/${start}/${end}`;
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
      query: (req: {
        workspaceId: string;
        teamId: string;
        workflowStatusId: string;
        page: number;
      }) => {
        const page = req.page ? req.page : 0;
        return `v1/task/list/${req.workspaceId}/${req.teamId}/with-workflow/${req.workflowStatusId}?page=${page}`;
      },
      providesTags: (_result, _err, req) => [
        {
          type: "team-workflow-task-list",
          id: `${JSON.stringify(req)}`,
        },
      ],
    }),
  }),
});

export const {
  useRetrieveAllIntersectingTasksQuery,
  useRetrieveFromWorkflowStatusQuery,
} = taskListingApi;

export const {
  endpoints: { retrieveAllIntersectingTasks, retrieveFromWorkflowStatus },
} = taskListingApi;
