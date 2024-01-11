import { useRetrieveWorkspaceTeamsQuery } from "@/store/api/teamApi";

export const useTeamFromName = (teamUsername: string, workspaceId?: string) => {
  const { data: teamsResponse } = useRetrieveWorkspaceTeamsQuery(workspaceId || "", {
    skip: workspaceId == null,
  });
  return teamsResponse?.data.find((teamDto) => teamDto.username == teamUsername);
};
