import { useWorkspaceRole } from "@/hooks/useWorkspaceRole";

export const useWorkspaceRoleIsAdminOrOwner = ({ workspaceId }: { workspaceId?: string }) => {
  const role = useWorkspaceRole({ workspaceId });
  return role && ["ADMIN", "OWNER"].indexOf(role) != -1;
};