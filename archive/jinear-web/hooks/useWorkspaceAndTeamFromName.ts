import { useTeamFromName } from "./useTeamFromName";
import { useWorkspaceFromName } from "./useWorkspaceFromName";

export const useWorkspaceAndTeamFromName = (workspaceName: string, teamUsername: string) => {
  const workspace = useWorkspaceFromName(workspaceName);
  const team = useTeamFromName(teamUsername, workspace?.workspaceId);
  return { workspace, team };
};
