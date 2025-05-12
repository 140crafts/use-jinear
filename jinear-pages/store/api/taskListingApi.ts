import { TaskFilterRequest, TaskListingPaginatedResponse } from "@/model/be/jinear-core";
import { api } from "./api";

export const taskListingApi = api.injectEndpoints({
  endpoints: (build) => ({
    filterTasks: build.query<TaskListingPaginatedResponse, TaskFilterRequest>({
      query: (req) => ({ url: `v1/task/list/filter`, method: "POST", body: req }),
      providesTags: (_result, _err, req) => [
        {
          type: "v1/task/list/filter",
          id: `${JSON.stringify(req)}`,
        },
      ],
    }),
    //
  }),
});

export const { useFilterTasksQuery } = taskListingApi;

export const {
  endpoints: { filterTasks },
} = taskListingApi;
