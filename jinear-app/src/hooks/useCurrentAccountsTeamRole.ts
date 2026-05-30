import { useRetrieveMembershipsQuery } from "@/store/api/teamMemberApi";

export const useCurrentAccountsTeamRole = (vo: { workspaceId?: string; teamId?: string }) => {
  const { data: membershipsResponse } = useRetrieveMembershipsQuery(
    { workspaceId: vo.workspaceId || "" },
    { skip: vo.workspaceId == null }
  );
  return membershipsResponse?.data.find((teamMember) => teamMember.teamId == vo.teamId)?.role;
};

export const useCurrentAccountsTeamRoleIsAdmin = (vo: { workspaceId?: string; teamId?: string }) => {
  const role = useCurrentAccountsTeamRole(vo);
  return role == "ADMIN";
};
