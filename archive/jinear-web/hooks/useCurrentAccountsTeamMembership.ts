import { useRetrieveMembershipsQuery } from "@/api/teamMemberApi";

export const useCurrentAccountsTeamMembership = (vo: { workspaceId?: string; teamId?: string }) => {
  const { teamId, workspaceId } = vo;
  const { data: membershipsResponse } = useRetrieveMembershipsQuery(
    { workspaceId: workspaceId || "" },
    {
      skip: workspaceId == null
    }
  );
  return workspaceId && teamId && membershipsResponse
    ? membershipsResponse?.data?.find((member) => member.teamId == teamId)
    : undefined;
};