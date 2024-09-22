import { BaseResponse, ProjectTeamOperationRequest } from "@/model/be/jinear-core";
import { api } from "./api";

export const projectTeamApi = api.injectEndpoints({
  endpoints: (build) => ({
    //
    updateAsProjectTeams: build.mutation<BaseResponse, ProjectTeamOperationRequest>({
      query: (body) => ({
        url: "v1/project/team/update-as",
        method: "PUT",
        body: body
      }),
      invalidatesTags: ["v1/project/query/assigned/{workspaceId}", "v1/project/query/all/{workspaceId}", "v1/project/query/archived/{workspaceId}", "v1/project/query/{projectId}"]
    })
    //
  })
});

export const {
  useUpdateAsProjectTeamsMutation
} = projectTeamApi;

export const {
  endpoints: { updateAsProjectTeams }
} = projectTeamApi;
