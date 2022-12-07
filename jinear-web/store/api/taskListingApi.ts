import {
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
  }),
});

export const { useRetrieveAllIntersectingTasksQuery } = taskListingApi;

export const {
  endpoints: { retrieveAllIntersectingTasks },
} = taskListingApi;
