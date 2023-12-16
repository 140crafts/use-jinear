import { TaskFeedItemResponse } from "@/model/be/jinear-core";
import { api } from "./api";

export const taskFeedItemApi = api.injectEndpoints({
  endpoints: (build) => ({
    retrieveTaskFeedItems: build.query<TaskFeedItemResponse, { taskId: string }>({
      query: (req) => `v1/task/feed-item/from-task/${req.taskId}`,
      providesTags: (_result, _err, req) => [
        {
          type: "v1/task/feed-item/from-task/req.taskId",
          id: `${req.taskId}`,
        },
      ],
    }),
    //
  }),
});

export const { useRetrieveTaskFeedItemsQuery } = taskFeedItemApi;

export const {
  endpoints: { retrieveTaskFeedItems },
} = taskFeedItemApi;
