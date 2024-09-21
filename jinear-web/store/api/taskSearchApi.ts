import { TaskSearchRequest, TaskSearchResponse } from "@/model/be/jinear-core";
import { api } from "./api";

export const taskSearchApi = api.injectEndpoints({
  endpoints: (build) => ({
    searchTask: build.query<TaskSearchResponse, { body: TaskSearchRequest; page?: number }>({
      query: (req) => {
        const body = req.body;
        const page = req.page ? req.page : 0;
        return {
          url: `v1/task/search/${body.workspaceId}?page=${page}`,
          method: "POST",
          body
        };
      },
      providesTags: (_result, _err, req) => [
        {
          type: "v1/task/search/${workspaceId}",
          id: `${JSON.stringify(req)}`
        }
      ]
    })
    //
  })
});

export const { useSearchTaskQuery } = taskSearchApi;

export const {
  endpoints: { searchTask }
} = taskSearchApi;
