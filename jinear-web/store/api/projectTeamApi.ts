import { BaseResponse, ProjectTeamOperationRequest } from "@/model/be/jinear-core";
import { api } from "./api";

export const projectTeamApi = api.injectEndpoints({
  endpoints: (build) => ({
    //
    assignProjectToTeam: build.mutation<BaseResponse, ProjectTeamOperationRequest>({
      query: (body) => ({
        url: "v1/project/team/assign",
        method: "POST",
        body: body
      }),
      invalidatesTags: ["v1/project/query/assigned/{workspaceId}", "v1/project/query/all/{workspaceId}"]
    }),
    //
    removeProjectFromTeam: build.mutation<BaseResponse, ProjectTeamOperationRequest>({
      query: (body) => ({
        url: "v1/project/team/remove",
        method: "DELETE",
        body: body
      }),
      invalidatesTags: ["v1/project/query/assigned/{workspaceId}", "v1/project/query/all/{workspaceId}"]
    })
    //
  })
});

export const {
  useAssignProjectToTeamMutation,
  useRemoveProjectFromTeamMutation
} = projectTeamApi;

export const {
  endpoints: { assignProjectToTeam, removeProjectFromTeam }
} = projectTeamApi;
