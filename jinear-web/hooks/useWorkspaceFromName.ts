import { selectWorkspaceFromWorkspaceUsername } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store/store";

export const useWorkspaceFromName = (workspaceName?: string) => {
  return useTypedSelector(selectWorkspaceFromWorkspaceUsername(workspaceName));
};
