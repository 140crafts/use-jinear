import {
  BaseResponse,
  ProjectDatesUpdateRequest,
  ProjectDescriptionUpdateRequest,
  ProjectInitializeRequest,
  ProjectPriorityUpdateRequest,
  ProjectStateUpdateRequest,
  ProjectTitleUpdateRequest,
  ProjectUpdateArchivedRequest,
  ProjectUpdateLeadRequest
} from "@/model/be/jinear-core";
import { api } from "./api";

export const projectOperationApi = api.injectEndpoints({
  endpoints: (build) => ({
    //
    initializeProject: build.mutation<BaseResponse, ProjectInitializeRequest>({
      query: (body) => ({
        url: "v1/project/operation",
        method: "POST",
        body: body
      }),
      invalidatesTags: ["v1/project/query/assigned/{workspaceId}", "v1/project/query/all/{workspaceId}", "v1/project/query/archived/{workspaceId}", `v1/project/query/{projectId}`]
    }),
    //
    updateProjectTitle: build.mutation<BaseResponse, { projectId: string, body: ProjectTitleUpdateRequest }>({
      query: ({ projectId, body }: { projectId: string, body: ProjectTitleUpdateRequest }) => ({
        url: `v1/project/operation/${projectId}/title`,
        method: "PUT",
        body: body
      }),
      invalidatesTags: ["v1/project/query/assigned/{workspaceId}", "v1/project/query/all/{workspaceId}", "v1/project/query/archived/{workspaceId}", `v1/project/query/{projectId}`, `v1/project/query/{projectId}/info`]
    }),
    //
    updateProjectDescription: build.mutation<BaseResponse, {
      projectId: string,
      body: ProjectDescriptionUpdateRequest
    }>({
      query: ({ projectId, body }: { projectId: string, body: ProjectDescriptionUpdateRequest }) => ({
        url: `v1/project/operation/${projectId}/description`,
        method: "PUT",
        body: body
      }),
      invalidatesTags: ["v1/project/query/assigned/{workspaceId}", "v1/project/query/all/{workspaceId}", "v1/project/query/archived/{workspaceId}", `v1/project/query/{projectId}`, `v1/project/query/{projectId}/info`]
    }),
    //
    updateProjectState: build.mutation<BaseResponse, { projectId: string, body: ProjectStateUpdateRequest }>({
      query: ({ projectId, body }: { projectId: string, body: ProjectStateUpdateRequest }) => ({
        url: `v1/project/operation/${projectId}/state`,
        method: "PUT",
        body: body
      }),
      invalidatesTags: ["v1/project/query/assigned/{workspaceId}", "v1/project/query/all/{workspaceId}", "v1/project/query/archived/{workspaceId}", `v1/project/query/{projectId}`, `v1/project/query/{projectId}/info`]
    }),
    //
    updateProjectPriority: build.mutation<BaseResponse, { projectId: string, body: ProjectPriorityUpdateRequest }>({
      query: ({ projectId, body }: { projectId: string, body: ProjectPriorityUpdateRequest }) => ({
        url: `v1/project/operation/${projectId}/priority`,
        method: "PUT",
        body: body
      }),
      invalidatesTags: ["v1/project/query/assigned/{workspaceId}", "v1/project/query/all/{workspaceId}", "v1/project/query/archived/{workspaceId}", `v1/project/query/{projectId}`, `v1/project/query/{projectId}/info`]
    }),
    //
    updateProjectDates: build.mutation<BaseResponse, { projectId: string, body: ProjectDatesUpdateRequest }>({
      query: ({ projectId, body }: { projectId: string, body: ProjectDatesUpdateRequest }) => ({
        url: `v1/project/operation/${projectId}/dates`,
        method: "PUT",
        body: body
      }),
      invalidatesTags: ["v1/project/query/assigned/{workspaceId}", "v1/project/query/all/{workspaceId}", "v1/project/query/archived/{workspaceId}", `v1/project/query/{projectId}`, `v1/project/query/{projectId}/info`]
    }),
    //
    updateProjectLead: build.mutation<BaseResponse, { projectId: string, body: ProjectUpdateLeadRequest }>({
      query: ({ projectId, body }: { projectId: string, body: ProjectUpdateLeadRequest }) => ({
        url: `v1/project/operation/${projectId}/lead`,
        method: "PUT",
        body: body
      }),
      invalidatesTags: ["v1/project/query/assigned/{workspaceId}", "v1/project/query/all/{workspaceId}", "v1/project/query/archived/{workspaceId}", `v1/project/query/{projectId}`, `v1/project/query/project-permissions/{projectId}`, `v1/project/query/{projectId}/info`]
    }),
    //
    updateProjectArchived: build.mutation<BaseResponse, { projectId: string, body: ProjectUpdateArchivedRequest }>({
      query: ({ projectId, body }: { projectId: string, body: ProjectUpdateArchivedRequest }) => ({
        url: `v1/project/operation/${projectId}/archived`,
        method: "PUT",
        body: body
      }),
      invalidatesTags: ["v1/project/query/assigned/{workspaceId}", "v1/project/query/all/{workspaceId}", "v1/project/query/archived/{workspaceId}", `v1/project/query/{projectId}`, `v1/project/query/{projectId}/info`]
    })
    //
  })
});

export const {
  useInitializeProjectMutation,
  useUpdateProjectTitleMutation,
  useUpdateProjectDescriptionMutation,
  useUpdateProjectStateMutation,
  useUpdateProjectPriorityMutation,
  useUpdateProjectDatesMutation,
  useUpdateProjectLeadMutation,
  useUpdateProjectArchivedMutation
} = projectOperationApi;

export const {
  endpoints: {
    initializeProject,
    updateProjectTitle,
    updateProjectDescription,
    updateProjectState,
    updateProjectPriority,
    updateProjectDates,
    updateProjectLead,
    updateProjectArchived
  }
} = projectOperationApi;
