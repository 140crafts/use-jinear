import { BaseResponse, TaskMediaResponse } from "@/model/be/jinear-core";
import { api } from "./api";

export interface IRetrieveTaskMediaListRequest {
  taskId: string;
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
    uploadTaskMedia: build.mutation<BaseResponse, IUploadTaskMediaRequest>({
      query: (req: IUploadTaskMediaRequest) => ({
        url: `v1/task/media/${req.taskId}/upload`,
        method: "POST",
        body: req.formData,
      }),
      invalidatesTags: (_result, _err, req) => [
        { type: "task-media-list", id: req.taskId },
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
      invalidatesTags: (_result, _err, req) => [{ type: "task-media-list", id: req.taskId }],
    }),
    //
    downloadTaskMedia: build.query<BaseResponse, IDownloadTaskMediaRequest>({
      query: (req: IDownloadTaskMediaRequest) => ({
        url: `v1/task/media/${req.taskId}/download/${req.mediaId}`,
        method: "GET",
      }),
      providesTags: (_result, _err, req) => [
        { type: "task-media-download", id: `${req.taskId}-${req.mediaId}` },
        "workspace-task-activity-list",
        "workspace-team-activity-list",
        "workspace-activity-list",
      ],
    }),
    //
  }),
});

export const {
  useRetrieveTaskMediaListQuery,
  useUploadTaskMediaMutation,
  useDeleteTaskMediaMutation,
  useDownloadTaskMediaQuery,
} = taskMediaApi;

export const {
  endpoints: { retrieveTaskMediaList, uploadTaskMedia, deleteTaskMedia, downloadTaskMedia },
} = taskMediaApi;
