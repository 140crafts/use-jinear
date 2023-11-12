import { TeamInitializeRequest, TeamListingResponse, TeamResponse, TeamTaskVisibilityType } from "@/model/be/jinear-core";
import { api } from "./api";

export const teamApi = api.injectEndpoints({
  endpoints: (build) => ({
    initializeTeam: build.mutation<TeamResponse, TeamInitializeRequest>({
      query: (body: TeamInitializeRequest) => ({
        url: `v1/team`,
        method: "POST",
        body,
      }),
      invalidatesTags: ["workplace-team-list", "workspace-activity-list"],
    }),
    //
    retrieveWorkspaceTeams: build.query<TeamListingResponse, string>({
      query: (workspaceId: string) => `v1/team/from-workspace/${workspaceId}`,
      providesTags: (_result, _err, workspaceId) => [
        {
          type: "workplace-team-list",
          id: workspaceId,
        },
        {
          type: "workplace-team-list",
          id: "ALL",
        },
      ],
    }),
    //
    updateTeamTaskVisibilityType: build.mutation<TeamResponse, { teamId: string; taskVisibilityType: TeamTaskVisibilityType }>({
      query: ({ teamId, taskVisibilityType }) => ({
        url: `v1/team/${teamId}/task-visibility-type/${taskVisibilityType}`,
        method: "PUT",
      }),
      invalidatesTags: [
        "workplace-task-with-name-and-tag",
        "team-task-list",
        "team-all-task-list",

        "team-workflow-task-list",
        "reminder-next-job",
        "workspace-task-list",
        "count-workspace-notification-events",
        "account-workspace-notification-unread-count",
        "task-checklist",
        "task-subscription",
        "task-subscription-subscriber-list",
        "task-list-with-assignee",
        "task-list-assigned-to-current-account",
        "workspace-activity-list",
        "workspace-team-activity-list",
        "workspace-task-activity-list",
        "task-list-listing",
        "task-list-entry-listing",
        "task-board-entry-listing",
        "retrieve-task-and-task-boards-relation",
        "team-topic-task-list",
        "task-listing-filter",
        "team-topic-search",
        "task-board-listing",
        "team-topic-find-exact",
        "retrieve-topic-by-tag",
        "task-board-filter",
        "task-media-list",
        "task-media-list-from-team",
        "payments-info-workspace-subscription",
        "task-comments",
        "workspace-team-membership-list",
      ],
    }),
    //
  }),
});

export const { useInitializeTeamMutation, useRetrieveWorkspaceTeamsQuery, useUpdateTeamTaskVisibilityTypeMutation } = teamApi;

export const {
  endpoints: { initializeTeam, retrieveWorkspaceTeams, updateTeamTaskVisibilityType },
} = teamApi;
