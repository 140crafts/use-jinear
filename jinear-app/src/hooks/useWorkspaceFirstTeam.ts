import { useRetrieveMembershipsQuery } from "@/store/api/teamMemberApi";

export const useWorkspaceFirstTeam = (workspaceId: string) => {
  const { data: membershipsResponse } = useRetrieveMembershipsQuery({ workspaceId: workspaceId });

  return workspaceId && membershipsResponse ? membershipsResponse?.data?.[0]?.team : undefined;
};
