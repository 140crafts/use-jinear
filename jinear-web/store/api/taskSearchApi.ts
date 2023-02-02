import { TaskSearchResponse } from "@/model/be/jinear-core";
import { api } from "./api";

export const taskSearchApi = api.injectEndpoints({
  endpoints: (build) => ({
    searchTask: build.query<
      TaskSearchResponse,
      { workspaceId: string; teamId: string; title: string; page?: number }
    >({
      query: (req) => {
        const { workspaceId, teamId, title } = req;
        const page = req.page ? req.page : 0;
        return `v1/task/search/${workspaceId}/${teamId}/${encodeURI(
          title
        )}?page=${page}`;
      },
      providesTags: (_result, _err, req) => [
        {
          type: "team-task-search",
          id: `${JSON.stringify(req)}`,
        },
      ],
    }),
    //
  }),
});

export const { useSearchTaskQuery } = taskSearchApi;

export const {
  endpoints: { searchTask },
} = taskSearchApi;
