import { useTypedSelector } from "@/store/store";
import { selectCurrentAccountsWorkspace } from "@/slice/accountSlice";

export const useWorkspace = (workspaceId: string) => useTypedSelector(selectCurrentAccountsWorkspace(workspaceId));