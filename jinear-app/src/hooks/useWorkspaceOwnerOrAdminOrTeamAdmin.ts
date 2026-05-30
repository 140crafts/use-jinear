import { useTeamRole } from "@/hooks/useTeamRole";
import { useWorkspaceRole } from "@/hooks/useWorkspaceRole";

export const useWorkspaceOwnerOrAdminOrTeamAdmin = ({ workspaceId, teamId }: {
  workspaceId?: string;
  teamId?: string
}) => {
  const workspaceRole = useWorkspaceRole({ workspaceId }) || "";
  const teamRole = useTeamRole({ teamId, workspaceId });
  return ["OWNER", "ADMIN"].indexOf(workspaceRole) != -1 || teamRole == "ADMIN";
};