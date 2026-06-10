import { selectWorkspaceFromWorkspaceUsername } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store";

export const useWorkspaceFromName = (workspaceName?: string) => {
  return useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));
};
