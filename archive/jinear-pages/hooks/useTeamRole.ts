import { useRetrieveMembershipsQuery } from "@/store/api/teamMemberApi";

export const useTeamRole = (vo: { workspaceId?: string; teamId?: string }) => {
  const { teamId, workspaceId } = vo;
  const { data: membershipsResponse } = useRetrieveMembershipsQuery(
    { workspaceId: workspaceId || "" },
    {
      skip: workspaceId == null,
    }
  );

  return workspaceId && teamId && membershipsResponse
    ? membershipsResponse?.data?.find((member) => member.teamId == teamId)?.role
    : undefined;
};
