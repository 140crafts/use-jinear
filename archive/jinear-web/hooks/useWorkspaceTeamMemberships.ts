import { useRetrieveMembershipsQuery } from "@/store/api/teamMemberApi";

export const useWorkspaceTeamMemberships = (workspaceId: string) => {
  const { data: membershipsResponse } = useRetrieveMembershipsQuery({ workspaceId: workspaceId });

  return workspaceId && membershipsResponse ? membershipsResponse?.data?.map(membership => membership.team) : undefined;
};
