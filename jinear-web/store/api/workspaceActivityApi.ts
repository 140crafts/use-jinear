import { WorkspaceActivityFilterRequest, WorkspaceActivityListResponse } from "@/model/be/jinear-core";
import { api } from "./api";

export const workspaceActivityApi = api.injectEndpoints({
  endpoints: (build) => ({
    filterWorkspaceActivities: build.query<WorkspaceActivityListResponse, WorkspaceActivityFilterRequest>({
      query: (req) => ({ url: `v1/workspace/activity/filter`, method: "POST", body: req }),
      providesTags: (_result, _err, req) => [
        {
          type: "workspace-activity-filtered-list",
          id: `${JSON.stringify(req)}`,
        },
      ],
    }),
    //
  }),
});

export const { useFilterWorkspaceActivitiesQuery } = workspaceActivityApi;

export const {
  endpoints: { filterWorkspaceActivities },
} = workspaceActivityApi;
