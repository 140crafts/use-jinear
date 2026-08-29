import type {
  BaseResponse,
  InitializeTeamWorkflowStatusRequest,
  TeamWorkflowStatusDto,
  TeamWorkflowStatusListingResponse,
  TeamWorkflowStatusReorderRequest,
} from "@/model/be/jinear-core";
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

    reorderTeamWorkflowStatuses: build.mutation<
      BaseResponse,
      {
        teamId: string;
        teamWorkflowStatusReorderRequest: TeamWorkflowStatusReorderRequest;
      }
    >({
      query: (request: { teamId: string; teamWorkflowStatusReorderRequest: TeamWorkflowStatusReorderRequest }) => ({
        url: `v1/team/workflow-status/${request.teamId}/reorder`,
        method: "PUT",
        body: request.teamWorkflowStatusReorderRequest,
      }),
      invalidatesTags: (_result, _err, req) => [{ type: "v1/team/workflow-status/{teamId}/list", id: req.teamId }],
      // Reorder the cached group up front so the dragged row settles into its new
      // position instead of snapping back until the refetch lands.
      async onQueryStarted({ teamId, teamWorkflowStatusReorderRequest }, { dispatch, queryFulfilled }) {
        const { workflowStateGroup, orderedTeamWorkflowStatusIds } = teamWorkflowStatusReorderRequest;
        const patchResult = dispatch(
          teamWorkflowStatusApi.util.updateQueryData("retrieveAllFromTeam", { teamId }, (draft) => {
            const statuses = draft.data?.groupedTeamWorkflowStatuses?.[workflowStateGroup];
            if (!statuses) {
              return;
            }
            const statusMap = new Map<string, TeamWorkflowStatusDto>(
              statuses.map((status) => [status.teamWorkflowStatusId, status])
            );
            const reordered = orderedTeamWorkflowStatusIds
              .map((teamWorkflowStatusId) => statusMap.get(teamWorkflowStatusId))
              .filter((status): status is TeamWorkflowStatusDto => status != null);
            if (reordered.length != statuses.length) {
              return;
            }
            reordered.forEach((status, index) => {
              status.order = index;
            });
            draft.data.groupedTeamWorkflowStatuses[workflowStateGroup] = reordered;
          })
        );
        try {
          await queryFulfilled;
        } catch {
          patchResult.undo();
        }
      },
    }),
  }),
});

export const {
  useRetrieveAllFromTeamQuery,
  useInitializeTeamWorkflowStatusMutation,
  useRemoveTeamWorkflowStatusMutation,
  useChangeTeamWorkflowStatusNameMutation,
  useChangeOrderMutation,
  useReorderTeamWorkflowStatusesMutation,
} = teamWorkflowStatusApi;

export const {
  endpoints: {
    retrieveAllFromTeam,
    initializeTeamWorkflowStatus,
    removeTeamWorkflowStatus,
    changeTeamWorkflowStatusName,
    changeOrder,
    reorderTeamWorkflowStatuses,
  },
} = teamWorkflowStatusApi;
