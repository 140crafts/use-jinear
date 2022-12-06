import {
  TaskRetrieveIntersectingRequest,
  TopicListingResponse,
} from "@/model/be/jinear-core";
import { api } from "./api";

export const taskListingApi = api.injectEndpoints({
  endpoints: (build) => ({
    retrieveAllIntersectingTasks: build.query<
      TopicListingResponse,
      TaskRetrieveIntersectingRequest
    >({
      query: (req) => {
        const { workspaceId, teamId } = req;
        const start = req.timespanStart.toISOString();
        const end = req.timespanEnd.toISOString();
        return `v1/task/list/${workspaceId}/${teamId}/intersecting/${start}/${end}`;
      },
      providesTags: (_result, _err, req) => [
        {
          type: "team-task-list",
          id: `${req.teamId}-${
            req.workspaceId
          }-${req.timespanStart.getTime()}-${req.timespanEnd.getTime()}`,
        },
      ],
    }),
  }),
});

export const { useRetrieveAllIntersectingTasksQuery } = taskListingApi;

export const {
  endpoints: { retrieveAllIntersectingTasks },
} = taskListingApi;
