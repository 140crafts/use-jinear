import { TaskActivityRetrieveResponse } from "@/model/be/jinear-core";
import { api } from "./api";

export const taskApi = api.injectEndpoints({
  endpoints: (build) => ({
    //
    retrieveTaskActivity: build.query<
      TaskActivityRetrieveResponse,
      {
        taskId: string;
      }
    >({
      query: (req: { taskId: string }) => `v1/task/activity/${req.taskId}`,
      providesTags: (_result, _err, req) => [
        {
          type: "retrieve-task-activity",
          id: `${req.taskId}`,
        },
      ],
    }),
    //
  }),
});

export const { useRetrieveTaskActivityQuery } = taskApi;

export const {
  endpoints: { retrieveTaskActivity },
} = taskApi;
