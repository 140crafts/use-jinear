import { useTypedSelector } from "@/store/store";
import { selectCurrentAccountsWorkspaces } from "@/slice/accountSlice";

export const useWorkspaceRole = ({ workspaceId }: { workspaceId?: string }) => {
  return useTypedSelector(selectCurrentAccountsWorkspaces)?.find(w => w.workspaceId == workspaceId)?.role;
};