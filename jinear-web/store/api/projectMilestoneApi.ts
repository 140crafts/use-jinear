import { BaseResponse, InitializeMilestoneRequest, MilestoneUpdateRequest } from "@/model/be/jinear-core";
import { api } from "./api";

export const projectMilestoneApi = api.injectEndpoints({
  endpoints: (build) => ({
    //
    initializeMilestone: build.mutation<BaseResponse, InitializeMilestoneRequest>({
      query: (body) => ({
        url: "v1/project/milestone",
        method: "POST",
        body: body
      }),
      invalidatesTags: ["v1/project/query/assigned/{workspaceId}", "v1/project/query/all/{workspaceId}", `v1/project/query/{projectId}`]
    }),
    //
    updateMilestoneTitle: build.mutation<BaseResponse, MilestoneUpdateRequest>({
      query: (body) => ({
        url: "v1/project/milestone/title",
        method: "PUT",
        body: body
      }),
      invalidatesTags: ["v1/project/query/assigned/{workspaceId}", "v1/project/query/all/{workspaceId}", `v1/project/query/{projectId}`]
    }),
    //
    updateMilestoneDescription: build.mutation<BaseResponse, MilestoneUpdateRequest>({
      query: (body) => ({
        url: "v1/project/milestone/description",
        method: "PUT",
        body: body
      }),
      invalidatesTags: ["v1/project/query/assigned/{workspaceId}", "v1/project/query/all/{workspaceId}", `v1/project/query/{projectId}`]
    }),
    //
    updateMilestoneTargetDate: build.mutation<BaseResponse, MilestoneUpdateRequest>({
      query: (body) => ({
        url: "v1/project/milestone/target-date",
        method: "PUT",
        body: body
      }),
      invalidatesTags: ["v1/project/query/assigned/{workspaceId}", "v1/project/query/all/{workspaceId}", `v1/project/query/{projectId}`]
    }),
    //
    updateMilestoneOrder: build.mutation<BaseResponse, MilestoneUpdateRequest>({
      query: (body) => ({
        url: "v1/project/milestone/order",
        method: "PUT",
        body: body
      }),
      invalidatesTags: ["v1/project/query/assigned/{workspaceId}", "v1/project/query/all/{workspaceId}", `v1/project/query/{projectId}`]
    }),
    //
    deleteMilestone: build.mutation<BaseResponse, string>({
      query: (milestoneId) => ({
        url: `v1/project/milestone/${milestoneId}`,
        method: "DELETE"
      }),
      invalidatesTags: [
        "v1/project/query/assigned/{workspaceId}",
        "v1/project/query/all/{workspaceId}",
        `v1/project/query/{projectId}`,
        "v1/task/from-workspace/{workspaceName}/{taskTag}"]
    })
    //
  })
});

export const {
  useInitializeMilestoneMutation,
  useUpdateMilestoneTitleMutation,
  useUpdateMilestoneDescriptionMutation,
  useUpdateMilestoneTargetDateMutation,
  useUpdateMilestoneOrderMutation,
  useDeleteMilestoneMutation
} = projectMilestoneApi;

export const {
  endpoints: {
    initializeMilestone,
    updateMilestoneTitle,
    updateMilestoneDescription,
    updateMilestoneTargetDate,
    updateMilestoneOrder,
    deleteMilestone
  }
} = projectMilestoneApi;
