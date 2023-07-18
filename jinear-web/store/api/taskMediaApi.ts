import { BaseResponse, TaskMediaResponse, TaskPaginatedMediaResponse } from "@/model/be/jinear-core";
import { api } from "./api";

export interface IRetrieveTaskMediaListRequest {
  taskId: string;
}

export interface IRetrieveTaskMediaListFromTeam {
  teamId: string;
  page?: number;
}

export interface IUploadTaskMediaRequest {
  taskId: string;
  formData?: FormData;
}

export interface IDeleteTaskMediaRequest {
  taskId: string;
  mediaId: string;
}

export interface IDownloadTaskMediaRequest {
  taskId: string;
  mediaId: string;
}

export const taskMediaApi = api.injectEndpoints({
  endpoints: (build) => ({
    retrieveTaskMediaList: build.query<TaskMediaResponse, IRetrieveTaskMediaListRequest>({
      query: ({ taskId }: IRetrieveTaskMediaListRequest) => `v1/task/media/${taskId}`,
      providesTags: (_result, _err, req) => [
        {
          type: "task-media-list",
          id: `${req.taskId}`,
        },
      ],
    }),
    //
    retrieveTaskMediaListFromTeam: build.query<TaskPaginatedMediaResponse, IRetrieveTaskMediaListFromTeam>({
      query: ({ teamId, page = 0 }: IRetrieveTaskMediaListFromTeam) => `v1/task/media/from-team/${teamId}?page=${page}`,
      providesTags: (_result, _err, { teamId, page = 0 }) => [
        {
          type: "task-media-list-from-team",
          id: `${teamId}-${page}`,
        },
      ],
    }),
    //
    uploadTaskMedia: build.mutation<BaseResponse, IUploadTaskMediaRequest>({
      query: (req: IUploadTaskMediaRequest) => ({
        url: `v1/task/media/${req.taskId}/upload`,
        method: "POST",
        body: req.formData,
      }),
      invalidatesTags: (_result, _err, req) => [
        { type: "task-media-list", id: req.taskId },
        "task-media-list-from-team",
        "workspace-task-activity-list",
        "workspace-team-activity-list",
        "workspace-activity-list",
      ],
    }),
    //
    deleteTaskMedia: build.mutation<BaseResponse, IDeleteTaskMediaRequest>({
      query: (req: IDeleteTaskMediaRequest) => ({
        url: `v1/task/media/${req.taskId}/delete/${req.mediaId}`,
        method: "DELETE",
      }),
      invalidatesTags: (_result, _err, req) => [
        { type: "task-media-list", id: req.taskId },
        "task-media-list-from-team",
        "workspace-task-activity-list",
        "workspace-team-activity-list",
        "workspace-activity-list",
      ],
    }),
    //
    downloadTaskMedia: build.query<BaseResponse, IDownloadTaskMediaRequest>({
      query: (req: IDownloadTaskMediaRequest) => ({
        url: `v1/task/media/${req.taskId}/download/${req.mediaId}`,
        method: "GET",
      }),
      providesTags: (_result, _err, req) => [{ type: "task-media-download", id: `${req.taskId}-${req.mediaId}` }],
    }),
    //
  }),
});

export const {
  useRetrieveTaskMediaListQuery,
  useRetrieveTaskMediaListFromTeamQuery,
  useUploadTaskMediaMutation,
  useDeleteTaskMediaMutation,
  useDownloadTaskMediaQuery,
} = taskMediaApi;

export const {
  endpoints: { retrieveTaskMediaList, uploadTaskMedia, deleteTaskMedia, downloadTaskMedia },
} = taskMediaApi;
