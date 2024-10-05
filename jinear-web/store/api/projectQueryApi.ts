import {
  AccountProjectPermissionFlagsResponse,
  ProjectListingPaginatedResponse,
  ProjectRetrieveResponse, PublicProjectRetrieveResponse
} from "@/model/be/jinear-core";
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
    allProjects: build.query<ProjectListingPaginatedResponse, { workspaceId: string, page?: number }>({
      query: ({ workspaceId, page = 0 }: {
        workspaceId: string,
        page?: number
      }) => `v1/project/query/all/${workspaceId}?page=${page}`,
      providesTags: (_result, _err, req) => [
        {
          type: `v1/project/query/all/{workspaceId}`,
          id: `${req.workspaceId}`
        }
      ]
    }),
    //
    archivedProjects: build.query<ProjectListingPaginatedResponse, { workspaceId: string, page?: number }>({
      query: ({ workspaceId, page = 0 }: {
        workspaceId: string,
        page?: number
      }) => `v1/project/query/archived/${workspaceId}?page=${page}`,
      providesTags: (_result, _err, req) => [
        {
          type: `v1/project/query/archived/{workspaceId}`,
          id: `${req.workspaceId}`
        }
      ]
    }),
    //
    retrieveProjectPermissions: build.query<AccountProjectPermissionFlagsResponse, { projectId: string }>({
      query: ({ projectId }: { projectId: string }) => `v1/project/query/project-permissions/${projectId}`,
      providesTags: (_result, _err, req) => [
        {
          type: `v1/project/query/project-permissions/{projectId}`,
          id: `${req.projectId}`
        }
      ]
    }),
    //
    retrievePublicProject: build.query<PublicProjectRetrieveResponse, { projectId: string }>({
      query: ({ projectId }: { projectId: string }) => `v1/project/query/${projectId}/info`,
      providesTags: (_result, _err, req) => [
        {
          type: `v1/project/query/{projectId}/info`,
          id: `${req.projectId}`
        }
      ]
    }),
    //
  })
});

export const {
  useRetrieveProjectQuery,
  useRetrieveAssignedProjectsQuery,
  useAllProjectsQuery,
  useArchivedProjectsQuery,
  useRetrieveProjectPermissionsQuery
} = projectQueryApi;

export const {
  endpoints: { retrieveProject, retrieveAssignedProjects, allProjects, archivedProjects, retrieveProjectPermissions }
} = projectQueryApi;
