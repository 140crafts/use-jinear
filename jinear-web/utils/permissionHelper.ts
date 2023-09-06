import { WorkspaceDto } from "@/model/be/jinear-core";

export const hasWorkspaceFilePermissions = (workspace?: WorkspaceDto | null) => {
  return workspace?.tier == "PRO";
};

export const isWorkspaceUpgradable = (workspace?: WorkspaceDto | null) => {
  return !workspace?.isPersonal && workspace?.tier == "BASIC";
};

export const isWorkspaceInPaidTier = (workspace?: WorkspaceDto | null) => {
  return workspace && !workspace?.isPersonal && workspace?.tier != "BASIC";
};
