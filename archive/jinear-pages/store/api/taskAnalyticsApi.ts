import { TaskNumbersResponse } from "@/model/be/jinear-core";
import { api } from "./api";

export const taskAnalyticsApi = api.injectEndpoints({
  endpoints: (build) => ({
    //
    retrieveTaskNumbers: build.query<TaskNumbersResponse, { workspaceId: string; teamId: string }>({
      query: (req: { workspaceId: string; teamId: string }) => `v1/task-analytics/${req.workspaceId}/team/${req.teamId}`,
      providesTags: (_result, _err, req) => [
        {
          type: "v1/task-analytics/{workspaceId}/team/{teamId}",
          id: req.workspaceId + req.teamId,
        },
      ],
    }),
    //
  }),
});

export const { useRetrieveTaskNumbersQuery } = taskAnalyticsApi;

export const {
  endpoints: { retrieveTaskNumbers },
} = taskAnalyticsApi;
