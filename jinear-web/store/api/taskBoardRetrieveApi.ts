import { TaskBoardRetrieveResponse } from "@/model/be/jinear-core";
import { api } from "./api";

interface ITaskBoardRetrieve {
  taskBoardId: string;
}

export const taskBoardRetrieveApi = api.injectEndpoints({
  endpoints: (build) => ({
    //
    retrieveTaskBoard: build.query<TaskBoardRetrieveResponse, ITaskBoardRetrieve>({
      query: ({ taskBoardId }: ITaskBoardRetrieve) => `v1/task-board/${taskBoardId}`,
      providesTags: (_result, _err, req) => [
        {
          type: "task-board-retrieve",
          id: `${req.taskBoardId}`,
        },
      ],
    }),
  }),
  //
});

export const { useRetrieveTaskBoardQuery } = taskBoardRetrieveApi;

export const {
  endpoints: { retrieveTaskBoard },
} = taskBoardRetrieveApi;
