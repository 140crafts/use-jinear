import { ProjectListingPaginatedResponse, ProjectRetrieveResponse } from "@/model/be/jinear-core";
import { api } from "./api";

export const projectQueryApi = api.injectEndpoints({
  endpoints: (build) => ({
    //
    retrieveProject: build.query<ProjectRetrieveResponse, { projectId: string }>({
      query: ({ projectId }: { projectId: string }) => `v1/project/query/${projectId}`,
      providesTags: (_result, _err, req) => [
        {
          type: `v1/project/query/{projectId}`,
          id: `${req.projectId}`
        }
      ]
    }),
    //
    retrieveAssignedProjects: build.query<ProjectListingPaginatedResponse, { workspaceId: string }>({
      query: ({ workspaceId }: { workspaceId: string }) => `v1/project/query/assigned/${workspaceId}`,
      providesTags: (_result, _err, req) => [
        {
          type: `v1/project/query/assigned/{workspaceId}`,
          id: `${req.workspaceId}`
        }
      ]
    }),
    //
    allProjects: build.query<ProjectListingPaginatedResponse, { workspaceId: string }>({
      query: ({ workspaceId }: { workspaceId: string }) => `v1/project/query/all/${workspaceId}`,
      providesTags: (_result, _err, req) => [
        {
          type: `v1/project/query/all/{workspaceId}`,
          id: `${req.workspaceId}`
        }
      ]
    })
    //
  })
});

export const { useRetrieveProjectQuery, useRetrieveAssignedProjectsQuery, useAllProjectsQuery } = projectQueryApi;

export const {
  endpoints: { retrieveProject, retrieveAssignedProjects, allProjects }
} = projectQueryApi;
