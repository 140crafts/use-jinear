import { selectCurrentAccountsWorkspaces } from "@/store/slice/accountSlice";
import { useTypedSelector } from "@/store";

export const useAccountsFirstWorkspace = () => {
  return useTypedSelector(selectCurrentAccountsWorkspaces)?.[0];
};
