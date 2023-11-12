import { TeamMemberRoleType, WorkspaceDto } from "@/model/be/jinear-core";

export const hasWorkspaceFilePermissions = (workspace?: WorkspaceDto | null) => {
  return workspace?.tier == "PRO";
};

export const hasWorkspaceTeamVisibilityTypeSelectAccess = (workspace?: WorkspaceDto | null) => {
  return workspace?.tier == "PRO";
};

export const isWorkspaceUpgradable = (workspace?: WorkspaceDto | null) => {
  return workspace?.tier == "BASIC";
};

export const isWorkspaceInPaidTier = (workspace?: WorkspaceDto | null) => {
  return workspace && workspace?.tier != "BASIC";
};

export const isTeamAdmin = (role: TeamMemberRoleType) => {
  return "ADMIN" == role;
};
