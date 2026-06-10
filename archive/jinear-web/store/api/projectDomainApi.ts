import { BaseResponse, ProjectDomainInitializeRequest } from "@/model/be/jinear-core";
import { api } from "./api";
import { IProjectPostInitializeRequest } from "@/api/projectPostApi";

export const projectDomainApi = api.injectEndpoints({
  endpoints: (build) => ({
    //
    initializeProjectDomain: build.mutation<BaseResponse, ProjectDomainInitializeRequest>({
      query: (request: ProjectDomainInitializeRequest) => ({
        url: "v1/project/domain",
        method: "POST",
        body: request
      }),
      invalidatesTags: [`v1/project/query/{projectId}`, `v1/project/query/all/{workspaceId}`]
    }),
    //
    deleteProjectDomain: build.mutation<BaseResponse, { projectDomainId: string }>({
      query: ({ projectDomainId }: { projectDomainId: string }) => ({
        url: `v1/project/domain/${projectDomainId}`,
        method: "DELETE"
      }),
      invalidatesTags: [`v1/project/query/{projectId}`, `v1/project/query/all/{workspaceId}`]
    })
    //
  })
});

export const { useInitializeProjectDomainMutation, useDeleteProjectDomainMutation } = projectDomainApi;

export const {
  endpoints: { initializeProjectDomain, deleteProjectDomain }
} = projectDomainApi;
