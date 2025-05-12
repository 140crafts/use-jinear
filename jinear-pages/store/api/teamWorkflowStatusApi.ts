import { BaseResponse, InitializeTeamWorkflowStatusRequest, TeamWorkflowStatusListingResponse } from "@/model/be/jinear-core";
import { api } from "./api";

export const teamWorkflowStatusApi = api.injectEndpoints({
  endpoints: (build) => ({
    retrieveAllFromTeam: build.query<
      TeamWorkflowStatusListingResponse,
      {
        teamId: string;
      }
    >({
      query: (req: { teamId: string }) => `v1/team/workflow-status/${req.teamId}/list`,
      providesTags: (_result, _err, req) => [
        {
          type: "v1/team/workflow-status/{teamId}/list",
          id: `${req.teamId}`,
        },
      ],
    }),

    initializeTeamWorkflowStatus: build.mutation<
      BaseResponse,
      {
        teamId: string;
        initializeTeamWorkflowStatusRequest: InitializeTeamWorkflowStatusRequest;
      }
    >({
      query: (request: { teamId: string; initializeTeamWorkflowStatusRequest: InitializeTeamWorkflowStatusRequest }) => ({
        url: `v1/team/workflow-status/${request.teamId}`,
        method: "POST",
        body: request.initializeTeamWorkflowStatusRequest,
      }),
      invalidatesTags: (_result, _err, req) => [{ type: "v1/team/workflow-status/{teamId}/list", id: req.teamId }],
    }),

    removeTeamWorkflowStatus: build.mutation<
      BaseResponse,
      {
        teamId: string;
        teamWorkflowStatusId: string;
      }
    >({
      query: (request: { teamId: string; teamWorkflowStatusId: string }) => ({
        url: `v1/team/workflow-status/${request.teamId}/${request.teamWorkflowStatusId}`,
        method: "DELETE",
      }),
      invalidatesTags: (_result, _err, req) => [{ type: "v1/team/workflow-status/{teamId}/list", id: req.teamId }],
    }),

    changeTeamWorkflowStatusName: build.mutation<
      BaseResponse,
      {
        teamId: string;
        teamWorkflowStatusId: string;
        name: string;
      }
    >({
      query: (request: { teamId: string; teamWorkflowStatusId: string; name: string }) => ({
        url: `v1/team/workflow-status/${request.teamId}/${request.teamWorkflowStatusId}`,
        method: "PUT",
        body: request,
      }),
      invalidatesTags: (_result, _err, req) => [{ type: "v1/team/workflow-status/{teamId}/list", id: req.teamId }],
    }),

    changeOrder: build.mutation<
      BaseResponse,
      {
        teamId: string;
        teamWorkflowStatusId: string;
        replaceWithTeamWorkflowStatusId: string;
      }
    >({
      query: (request: { teamId: string; teamWorkflowStatusId: string; replaceWithTeamWorkflowStatusId: string }) => ({
        url: `v1/team/workflow-status/${request.teamId}/change-order/${request.teamWorkflowStatusId}/with/${request.replaceWithTeamWorkflowStatusId}`,
        method: "PUT",
      }),
      invalidatesTags: (_result, _err, req) => [{ type: "v1/team/workflow-status/{teamId}/list", id: req.teamId }],
    }),
  }),
});

export const {
  useRetrieveAllFromTeamQuery,
  useInitializeTeamWorkflowStatusMutation,
  useRemoveTeamWorkflowStatusMutation,
  useChangeTeamWorkflowStatusNameMutation,
  useChangeOrderMutation,
} = teamWorkflowStatusApi;

export const {
  endpoints: {
    retrieveAllFromTeam,
    initializeTeamWorkflowStatus,
    removeTeamWorkflowStatus,
    changeTeamWorkflowStatusName,
    changeOrder,
  },
} = teamWorkflowStatusApi;
